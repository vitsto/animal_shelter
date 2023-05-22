package pro.sky.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.sky.entity.Client;
import pro.sky.entity.Pet;
import pro.sky.entity.Shelter;
import pro.sky.entity.User;
import pro.sky.exception.ClientNotFoundException;
import pro.sky.services.*;
import pro.sky.services.impl.SendMessageServiceImpl;
import pro.sky.services.impl.StartServiceImpl;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    @Getter
    private static final HashMap<Long, Shelter> clientIdToShelter = new HashMap<>();
    @Autowired
    private UserService userService;
    @Autowired
    private ShelterService shelterService;
    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private VolunteerService volunteerService;
    @Autowired
    private StorageService storageService;


    @Autowired
    private ClientService clientService;
    private StartService startService = new StartServiceImpl();
    private SendMessageService sendMessageService = new SendMessageServiceImpl();
    private final Pattern pattern = Pattern.compile("(\"\\D+\")\\s+(\"\\d{10,11}\")");
    boolean contactUserFlag;
    boolean contactClientFlag;

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
                case "/catShelter" -> {
                    clientIdToShelter.put(id, shelterService.chooseShelter("cat"));
                    telegramBot.execute(shelterService.giveMenu(id));
//                    telegramBot.execute(shelterService.start(shelter, id));
                }
                case "/dogShelter" -> {
                    clientIdToShelter.put(id, shelterService.chooseShelter("dog"));
                    telegramBot.execute(shelterService.giveMenu(id));
//                    telegramBot.execute(shelterService.start(shelter, id));
                }
                case "/stage1" -> {
                    telegramBot.execute(shelterService.start(clientIdToShelter.get(id), id));
                }
                case "/stage2" -> {
                    telegramBot.execute(recommendationService.menuForClientConsultation(id));
                }
                case "/stage3" -> {
                    PhotoSize[] photo = update.message().photo();
                    String description = update.message().text();
                    if (photo != null && description != null) {
                        PhotoSize photoSize = photo[0];
                        GetFileResponse execute = telegramBot.execute(new GetFile(photoSize.fileId()));
                        Set<Pet> petSet = clientService.findClient(id).orElseThrow(ClientNotFoundException::new).getPetSet();
                        if (petSet.size() > 1) {
                            // как отправить сообщение пользователю чтобы он выбрал конкретного животного, и как в дальнейшем обработать этот ввод?
                        } else {
                            String pathToFile = storageService.store(execute.file()); //файл сохранен, вернулся путь
                            reportService.createReport(petSet.stream().findAny().get(), description, pathToFile);
                        }
                    }  else if (photo != null) {
                        // запросить описание...опять же не понимаю как отправить польз-лю сообщение и с этого момента его обрабтать
                    } else if (description != null) {
                        // запросить фото...опять же не понимаю как отправить польз-лю сообщение и с этого момента его обрабтать
                    }
//                    telegramBot.execute();
                }

                case "/about" ->
                        sendResponse = clientIdToShelter.containsKey(id) ? telegramBot.execute(shelterService.aboutShelter(clientIdToShelter.get(id), id)) : telegramBot.execute(sendMessageService.shelterNotChoose(id));
                case "/info" ->
                        sendResponse = clientIdToShelter.containsKey(id) ? telegramBot.execute(shelterService.infoShelter(clientIdToShelter.get(id), id)) : telegramBot.execute(sendMessageService.shelterNotChoose(id));

                case "/guard" ->
                        sendResponse = clientIdToShelter.containsKey(id) ? telegramBot.execute(shelterService.getGuardContact(clientIdToShelter.get(id), id)) : telegramBot.execute(sendMessageService.shelterNotChoose(id));
                case "/contact" -> {
                    if (clientIdToShelter.containsKey(id)) {
                        telegramBot.execute(sendMessageService.send(id,
                                "Введите контактные данные в формате: \"Фамилия Имя Отчество\" \"номер телефона\".\nК примеру: \"Иванов Иван Иванович\" \"89990001122\""));
                        contactUserFlag = true;
                    } else {
                        contactUserFlag = false;
                    }
                }

                case "/volunteer" -> {
                    sendResponse = clientIdToShelter.containsKey(id) ? telegramBot.execute(volunteerService.callVolunteer(id, clientIdToShelter.get(id))) : telegramBot.execute(sendMessageService.shelterNotChoose(id));
                }
//                case "/safety" -> ;

                case "/recommendations" -> {
                    sendResponse = clientIdToShelter.containsKey(id) ? telegramBot.execute(recommendationService.giveRecommendation(id, clientIdToShelter.get(id))) : telegramBot.execute(sendMessageService.shelterNotChoose(id));
                }
                case "/giveDataToBot" -> {
                    if (clientIdToShelter.containsKey(id)) {
                        telegramBot.execute(sendMessageService.send(id,
                                """
                                        Отправь боту свои фамилию, имя, телефонный номер (из 11 цифр) и адрес
                                        в следующем формате: Иванов Иван 89991234567 Москва, ул. Дальняя, д.5, кв. 10
                                        """));
                        contactClientFlag = true;
                    } else {
                        contactClientFlag = false;
                    }
                }

                default -> {
                    Matcher matcher = pattern.matcher(message);

                    if (contactUserFlag && matcher.find()) {
                        User user = new User(matcher.group(1).replace("\"", ""),
                                matcher.group(2).replace("\"", ""), clientIdToShelter.get(id));
                        userService.writeContact(user);
                        telegramBot.execute(sendMessageService.send(id, "Спасибо! С Вами свяжутся"));
                    } else if (contactClientFlag) {
                        Client client = clientService.parseClientData(id, message);
                        clientService.saveClientToRepository(client);
                        telegramBot.execute(sendMessageService.send(id, "Спасибо! С Вами свяжутся"));
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
     * Метод получения идентификатора пользователя из сообщения.
     *
     * @param update сообщение, поступившее в бот.
     * @return Long id — идентификатор пользователя.
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
     * Метод получения текста сообщения, отправленного пользователем в бот.
     *
     * @param update сообщение, поступившее в бот.
     * @return message — сообщение.
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
