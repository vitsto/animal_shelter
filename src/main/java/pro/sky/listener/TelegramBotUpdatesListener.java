package pro.sky.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.sky.entity.Client;
import pro.sky.entity.Shelter;
import pro.sky.service.ClientService;
import pro.sky.service.RecommendationService;
import pro.sky.service.ShelterService;
import pro.sky.service.VolunteerService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для обработки сообщений (апдейтов), поступающих от пользователей в Telegram-бот.
 */
@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    /**
     * Переменная интерфейса Logger для обеспечения логирования событий, происходящих в коде.
     */
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    /**
     * HashMap для сохранения выбора приюта потенциальным клиентом.
     */
    private static final HashMap<Long, Long> clientIdToShelterId = new HashMap<>();
    private final Pattern pattern = Pattern.compile("([А-я\\s]+)\\s+(\\d{11})\\s+([А-я\\d\\s.,]+)");
    private final RecommendationService recommendationService;
    private final VolunteerService volunteerService;
    private final ClientService clientService;

    private final ShelterService shelterService;

    boolean contactFlag;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      RecommendationService recommendationService,
                                      VolunteerService volunteerService,
                                      ClientService clientService,
                                      ShelterService shelterService) {
        this.telegramBot = telegramBot;
        this.recommendationService = recommendationService;
        this.volunteerService = volunteerService;
        this.clientService = clientService;
        this.shelterService = shelterService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }


    /**
     * Метод первичной обработки поступивших в бот апдейтов.
     * @param updates список всех поступивших апдейтов.
     * @return количество обработанных апдейтов.
     */
    @Override
    public int process(List<Update> updates) {
            updates.stream()
                    .filter(update -> update.message() != null || update.callbackQuery() != null)
                    .forEach(this::handleUpdate);

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Метод обрабатывает апдейт /start, даёт пользователю возможность выбрать приют и сформировать запрос.
     * @param update сообщение, поступившее в бот.
     */
    private void handleUpdate(Update update) {
        try {
        logger.info("Обработан update: " + update);

            if (update.message() != null && update.message().text().startsWith("/start")) {
                    InlineKeyboardButton catShelter = new InlineKeyboardButton("Приют для кошек").callbackData("/catShelter");
                    InlineKeyboardButton dogShelter = new InlineKeyboardButton("Приют для собак").callbackData("/dogShelter");
                    InlineKeyboardMarkup shelterKeyboard = new InlineKeyboardMarkup(catShelter, dogShelter);
                    this.telegramBot.execute(new SendMessage(update.message().chat().id(), """
                            Привет! Это бот приюта для животных из Астаны.
                            Я отвечу на твои вопросы и расскажу, как забрать животное к себе домой.
                            Для начала выбери приют. Выбрать оба нельзя.""").replyMarkup(shelterKeyboard));
                    return;
                }
            if (update.callbackQuery().data().startsWith("/catShelter")) {
                this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(), "Выбран приют для кошек")
                        .replyMarkup(requestKeyboardHandler()));
                clientIdToShelterId.put(update.callbackQuery().from().id(), 2L);
            } else if (update.callbackQuery().data().startsWith("/dogShelter")){
                this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(), "Выбран приют для собак")
                        .replyMarkup(requestKeyboardHandler()));
                clientIdToShelterId.put(update.callbackQuery().from().id(), 1L);
            }
            clientChoiceHandler(update);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Метод вызова клавиатуры для формирования запроса пользователя.
     * Используется в методе {@link #handleUpdate(Update)}
     * @return клавиатура.
     */
    private Keyboard requestKeyboardHandler() {
        InlineKeyboardButton stage1 = new InlineKeyboardButton("Узнать информацию о приюте").callbackData("/stage1");
        InlineKeyboardButton stage2 = new InlineKeyboardButton("Как взять животное из приюта").callbackData("/stage2");
        InlineKeyboardButton stage3 = new InlineKeyboardButton("Прислать отчёт о питомце").callbackData("/stage3");
        InlineKeyboardButton callVolunteer = new InlineKeyboardButton("Позвать волонтёра").callbackData("/callVolunteer");
        return new InlineKeyboardMarkup().addRow(stage1, stage2).addRow(stage3, callVolunteer);
    }

    /**
     * Вспомогательный метод для определения запроса пользователя после выбора приюта.
     * @param update сообщение, поступившее в бот.
     */
    private void clientChoiceHandler(Update update) {

        // В 1-ом, 3-ем условиях и при вызове волонтера надо добавить переход на соответствующие методы.

        if (update.callbackQuery().data().startsWith("/stage1") ) {
            this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(),
                    "Вы хотите узнать информацию о приюте"));

        } else if (update.callbackQuery().data().startsWith("/stage2")) {
            this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(),
                    """
                            Я расскажу, как взять животное из приюта,
                            помогу разобраться с бюрократическими
                            и бытовыми вопросами.
                            """));
            recommendationService.giveRecommendation(update, clientIdToShelterId);

            this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(),
                    """
                            Если не нашёл ответ на свой вопрос,
                            обратись к волонтёру.
                            """).replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton("Позвать волонтёра").callbackData("/callVolunteer"))));
            // Написать реализацию метода в volunteerService
                volunteerService.callVolunteer();

            InlineKeyboardMarkup buttonToGiveDataToBot = new InlineKeyboardMarkup(new InlineKeyboardButton(
                    ("Оставить контактные данные")).callbackData("/giveDataToBot"));
            this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(),
                    """
                            Можешь оставить контактные данные для связи.
                            Отправь боту свои фамилию, имя, телефонный номер (из 11 цифр) и адрес
                            в следующем формате: Иванов Иван 89991234567 Москва, ул. Дальняя, д.5, кв. 10.
                            """).replyMarkup(buttonToGiveDataToBot));
            if (update.callbackQuery().data().startsWith("/giveDataToBot")) {

            }
            update.message().contact();
            clientService.saveClientToRepository(parseClientData(update));

        } else if (update.callbackQuery().data().startsWith("/stage3")) {
            this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(),
                    "Вы хотите прислать отчёт о питомце"));

        } else if (update.callbackQuery().data().startsWith("/callVolunteer")) {
            this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(),
                    "Вы хотите позвать волонтёра"));
            // Написать реализацию метода в volunteerService
                volunteerService.callVolunteer();
        }
    }

    /**
     * Метод обработки контактных данных для связи, отправленных потенциальным клиентом в бот.
     * Используется в методе {@link #clientChoiceHandler(Update)}
     * @param update сообщение, поступившее в бот.
     * @return сущность класса Client.
     */
    private Client parseClientData(Update update) {
        String clientData = update.callbackQuery().data();
        Client client = new Client();
        if (clientData != null) {
            Matcher matcher = pattern.matcher(clientData);
            if (matcher.find()) {
                String name = matcher.group(1);
                String phoneNumber = matcher.group(2);
                String address = matcher.group(3);
                client.setId(update.callbackQuery().from().id());
                client.setName(name);
                client.setPhoneNumber(phoneNumber);
                client.setAddress(address);
                new SendMessage(update.message().chat().id(), "Данные успешно сохранены.");
            } else {
                new SendMessage(update.message().chat().id(), "Некорректный формат данных!");
            }
        }
        return client;
    }

}
