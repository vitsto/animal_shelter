package pro.sky.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.repository.RecommendationRepository;
import pro.sky.repository.ShelterRepository;
import pro.sky.service.RecommendationService;

import java.util.HashMap;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final TelegramBot telegramBot;
    private final RecommendationRepository recommendationRepository;
    private final ShelterRepository shelterRepository;

    public RecommendationServiceImpl(TelegramBot telegramBot,
                                     RecommendationRepository recommendationRepository,
                                     ShelterRepository shelterRepository) {
        this.telegramBot = telegramBot;
        this.recommendationRepository = recommendationRepository;
        this.shelterRepository = shelterRepository;
    }

    /**
     * Метод получения рекомендаций в зависимости от вида приюта, выбранного пользователем.
     * @param update сообщение, поступившее в бот.
     * @param clientIdToShelterId мапа для сохранения приюта, выбранного пользователем.
     */
    @Override
    public void giveRecommendation(Update update, HashMap<Long, Long> clientIdToShelterId) {
        InlineKeyboardButton firstMeetWithPet = new InlineKeyboardButton("Первое знакомство с животным в приюте")
                .url("https://clck.ru/34Gguo").callbackData("/firstMeetWithPet");
        InlineKeyboardButton documentsList = new InlineKeyboardButton("Необходимые документы, чтобы взять животное из приюта")
                .url("https://clck.ru/34Ggwt").callbackData("/documentsList");
        InlineKeyboardButton transportation = new InlineKeyboardButton("Как правильно перевозить животных")
                .url("https://clck.ru/34Ggyv").callbackData("/transportation");
        InlineKeyboardButton gettingPuppyRules = new InlineKeyboardButton("Как обустроить дом для щенка")
                .url("https://clck.ru/34Gh34").callbackData("/gettingPuppyRules");
        InlineKeyboardButton gettingKittyRules = new InlineKeyboardButton("Как обустроить дом для котёнка")
                .url("https://clck.ru/34Gh4G").callbackData("/gettingKittyRules");
        InlineKeyboardButton gettingAdultPet = new InlineKeyboardButton("Как обустроить дом для взрослого животного")
                .url("https://clck.ru/34Gh6D").callbackData("/gettingAdultPet");
        InlineKeyboardButton gettingDisabledPet = new InlineKeyboardButton("Как обустроить дом для животного " +
                "с ограниченными возможностями (зрение, передвижение)")
                .url("https://clck.ru/34GhcR").callbackData("/gettingDisabledPet");
        InlineKeyboardButton dogTrainerAdvice = new InlineKeyboardButton("Советы кинолога по первичному общению с собакой")
                .url("https://clck.ru/34GhfW").callbackData("/dogTrainerAdvice");
        InlineKeyboardButton dogTrainersList = new InlineKeyboardButton("Кинологи, к которым можно обратиться")
                .url("https://clck.ru/34Ghox").callbackData("/dogTrainersList");
        InlineKeyboardButton refusalReasonsList = new InlineKeyboardButton("Почему могут отказать и не дать забрать собаку из приюта")
                .url("https://clck.ru/34Ghs9").callbackData("/refusalReasonsList");

        InlineKeyboardMarkup keyboardForDogShelter = new InlineKeyboardMarkup()
                .addRow(firstMeetWithPet)
                .addRow(documentsList)
                .addRow(transportation)
                .addRow(gettingPuppyRules)
                .addRow(gettingAdultPet)
                .addRow(gettingDisabledPet)
                .addRow(dogTrainerAdvice)
                .addRow(dogTrainersList)
                .addRow(refusalReasonsList);

        InlineKeyboardMarkup keyboardForCatShelter = new InlineKeyboardMarkup()
                .addRow(firstMeetWithPet)
                .addRow(documentsList)
                .addRow(transportation)
                .addRow(gettingKittyRules)
                .addRow(gettingAdultPet)
                .addRow(gettingDisabledPet);

        if (!clientIdToShelterId.containsValue(2L)) {
            this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(), """
            Вот перечень рекомендаций, которые могут пригодиться.""").replyMarkup(keyboardForDogShelter));
        } else if (!clientIdToShelterId.containsValue(1L)) {
            this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(), """
            Вот перечень рекомендаций, которые могут пригодиться.""").replyMarkup(keyboardForCatShelter));
        }
    }
}
