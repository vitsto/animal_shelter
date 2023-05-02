package pro.sky.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.sky.entity.Shelter;
import pro.sky.entity.User;
import pro.sky.services.SendMessageService;
import pro.sky.services.ShelterService;
import pro.sky.services.StartService;
import pro.sky.services.UserService;
import pro.sky.services.impl.SendMessageServiceImpl;
import pro.sky.services.impl.StartServiceImpl;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
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
    @Autowired
    private UserService userService;
    @Autowired
    private ShelterService shelterService;
    private StartService startService = new StartServiceImpl();
    private SendMessageService sendMessageService = new SendMessageServiceImpl();
    private final Pattern pattern = Pattern.compile("(\"\\D+\")\\s+(\"\\d{10,11}\")");
    private Shelter shelter;
    boolean contactFlag;

    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }


    /**
     * Метод первичной обработки поступивших в бот апдейтов.
     *
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
     * Метод обрабатывает поступившие сообщения.
     *
     * @param update сообщение, поступившее в бот.
     */
    private void handleUpdate(Update update) {
        try {
            logger.info("Обработан update: " + update);
            SendResponse sendResponse;
            Long id = getId(update);
            String message = getMessage(update);

            switch (message) {
                case "/start" -> sendResponse = telegramBot.execute(startService.start(id));
                case "/about" ->
                        sendResponse = !Objects.isNull(shelter) ? telegramBot.execute(shelterService.aboutShelter(shelter, id)) : telegramBot.execute(sendMessageService.shelterNotChoose(id));
                case "/info" ->
                        sendResponse = !Objects.isNull(shelter) ? telegramBot.execute(shelterService.infoShelter(shelter, id)) : telegramBot.execute(sendMessageService.shelterNotChoose(id));
                case "/catShelter" -> {
                    shelter = shelterService.chooseShelter("Cat");
                    telegramBot.execute(shelterService.start(shelter, id));
                }
                case "/dogShelter" -> {
                    shelter = shelterService.chooseShelter("Dog");
                    telegramBot.execute(shelterService.start(shelter, id));
                }
                case "/guard" ->
                        sendResponse = !Objects.isNull(shelter) ? telegramBot.execute(shelterService.getGuardContact(shelter, id)) : telegramBot.execute(sendMessageService.shelterNotChoose(id));
                case "/contact" ->
//                        sendResponse = !Objects.isNull(shelter) ? telegramBot.execute(userService.writeContact(shelter, id)) : telegramBot.execute(incorrectMessageService.shelterNotChoose(id));
                {
                    if (!Objects.isNull(shelter)) {
                        telegramBot.execute(sendMessageService.send(id, "Введите контактные данные в формате: \"Фамилия Имя Отчество\" \"номер телефона\".\nК примеру: \"Иванов Иван Иванович\" \"89990001122\""));
                        contactFlag = true;
                    } else {
                        contactFlag = false;
                    }
                }
//                case "/volunteer" -> ;
//                case "/safety" -> ;
                default -> {
                    Matcher matcher = pattern.matcher(message);
                    if (contactFlag && matcher.find()) {
                        User user = new User(matcher.group(1), matcher.group(2), shelter);
                        userService.writeContact(user);
                    } else {
                        telegramBot.execute(sendMessageService.commandIncorrect(id));
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * Метод, получения идентификатора пользователя из сообщения.
     *
     * @param update сообщение, поступившее в бот.
     * @return Long id идентификатор пользователя.
     */
    private Long getId(Update update) {
        Long id;
        if (update.callbackQuery() != null) {
            id = update.callbackQuery().from().id();
        } else {
            id = update.message().from().id();
        }
        return id;
    }

    /**
     * Получение сообщения от пользователя.
     *
     * @param update сообщение, поступившее в бот.
     * @return String сообщение.
     */
    private String getMessage(Update update) {
        String message = "error";
        if (!Objects.isNull(update.message())) {
            message = update.message().text();
        } else if (update.callbackQuery().data() != null) {
            message = update.callbackQuery().data();
        }
        return message;
    }
}
