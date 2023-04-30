package pro.sky.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.sky.entity.PetType;
import pro.sky.entity.Shelter;
import pro.sky.repository.PetTypeRepository;
import pro.sky.repository.ShelterRepository;
import pro.sky.services.IncorrectMessageService;
import pro.sky.services.ShelterService;
import pro.sky.services.StartService;
import pro.sky.services.impl.IncorrectMessageServiceImpl;
import pro.sky.services.impl.ShelterServiceImpl;
import pro.sky.services.impl.StartServiceImpl;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private ShelterRepository shelterRepository;
    @Autowired
    private PetTypeRepository petTypeRepository;
    private ShelterService shelterService = new ShelterServiceImpl();
    private StartService startService = new StartServiceImpl();
    private IncorrectMessageService incorrectMessageService = new IncorrectMessageServiceImpl();
    private Shelter shelter;

    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
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
     * Метод обрабатывает поступившие сообщения.
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
                case "/about" -> sendResponse = !Objects.isNull(shelter) ? telegramBot.execute(shelterService.aboutShelter(shelter, id)) : telegramBot.execute(incorrectMessageService.shelterNotChoose(id));
                case "/info" -> sendResponse = !Objects.isNull(shelter) ? telegramBot.execute(shelterService.infoShelter(shelter, id)) : telegramBot.execute(incorrectMessageService.shelterNotChoose(id));
                case "/catShelter" -> chooseShelter("Cat", id);
                case "/dogShelter" -> chooseShelter("Dog", id);
                case "/guard" -> sendResponse = !Objects.isNull(shelter) ? telegramBot.execute(shelterService.getGuardContact(shelter, id)) : telegramBot.execute(incorrectMessageService.shelterNotChoose(id));
//                case "/safety" -> ;
//                case "/contact" -> ;
//                case "/volunteer" -> ;
                default -> sendResponse = telegramBot.execute(incorrectMessageService.commandIncorrect(id));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * Метод отвечает за выбор приюта, соответствующего определённому типу и вывод списка возможных действий.
     * @param type тип приюта.
     * @param id идентификатор пользователя, для ответа.
     */
    private void chooseShelter(String type, Long id) {
        Optional<PetType> petType = Optional.ofNullable(petTypeRepository.findPetTypeByTypeName(type));
        if (petType.isPresent()) {
            shelter = shelterRepository.findShelterByPetTypeIs(petType.get());
            telegramBot.execute(shelterService.start(shelter, id));
        }
    }

    /**
     * Метод, получения идентификатора пользователя из сообщения.
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
