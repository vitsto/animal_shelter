package pro.sky.services.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.entity.Shelter;
import pro.sky.repository.RecommendationRepository;
import pro.sky.repository.ShelterRepository;
import pro.sky.services.RecommendationService;

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
     * Меню для консультации потенциального хозяина животного.
     *
     * @param fromId идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage menuForClientConsultation(Long fromId) {
        InlineKeyboardMarkup keyboardForConsultation = new InlineKeyboardMarkup().addRow(
                new InlineKeyboardButton("Получить рекомендации").callbackData("/recommendations")).addRow(
                new InlineKeyboardButton("Оставить контактные данные").callbackData("/giveDataToBot")).addRow(
                new InlineKeyboardButton("Позвать волонтёра").callbackData("/volunteer")
        );
        String message = """
                Я расскажу, как взять животное из приюта,
                помогу разобраться с бюрократическими
                и бытовыми вопросами.
                Выбери интересующий пункт меню:
                """;
        return new SendMessage(fromId, message).replyMarkup(keyboardForConsultation);
    }

    /**
     * Метод получения рекомендаций в зависимости от вида приюта, выбранного пользователем.
     *
     * @param fromId идентификатор пользователя.
     * @param shelter приют, выбранный пользователем.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage giveRecommendation(Long fromId, Shelter shelter) {
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

        SendMessage message = null;

        switch (shelter.getPetType().getTypeName()) {
            case "dog" -> {
                message = new SendMessage(fromId, """
                    Вот перечень рекомендаций, которые могут пригодиться.""").replyMarkup(keyboardForDogShelter);
            }
            case "cat" -> {
                message = new SendMessage(fromId, """
                    Вот перечень рекомендаций, которые могут пригодиться.""").replyMarkup(keyboardForCatShelter);
            }
        }
        return message;
    }
}
