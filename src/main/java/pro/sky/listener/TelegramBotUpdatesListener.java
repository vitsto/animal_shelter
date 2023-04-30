package pro.sky.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.sky.service.RecommendationService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Map<Long, Long> clientIdToShelterId = new HashMap<>();
    private final RecommendationService recommendationService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, RecommendationService recommendationService) {
        this.telegramBot = telegramBot;
        this.recommendationService = recommendationService;
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
        return new InlineKeyboardMarkup(stage1, stage2, stage3, callVolunteer);
    }

    /**
     * Вспомогательный метод для определения запроса пользователя после выбора приюта.
     * @param update сообщение, поступившее в бот.
     */
    private void clientChoiceHandler(Update update) {

        // В 1-ом, 3-ем условиях и при вызове волонтера надо добавить переход на соответствующие классы.

        if (update.callbackQuery().data().startsWith("/stage1") ) {
            this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(),
                    "Вы хотите узнать информацию о приюте"));
        } else if (update.callbackQuery().data().startsWith("/stage2")) {
            this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(),
                    "Я расскажу, как взять животное из приюта, " +
                            "помогу разобраться с бюрократическими " +
                            "и бытовыми вопросами."));
            recommendationService.giveRecommendation(update);
        } else if (update.callbackQuery().data().startsWith("/stage3")) {
            this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(),
                    "Вы хотите прислать отчёт о питомце"));
        } else if (update.callbackQuery().data().startsWith("/callVolunteer")) {
            this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(),
                    "Вы хотите позвать волонтёра"));
        }
    }

}
