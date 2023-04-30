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

    @Override
    public void giveRecommendation(Update update) {


        InlineKeyboardButton safetyRules = new InlineKeyboardButton("Рекомендации по технике безопасности на территории приюта")
                .url("https://clck.ru/34GgpW").callbackData("/safetyRules");
        InlineKeyboardButton firstMeetWithPet = new InlineKeyboardButton("Правила знакомства с животным до того, как забрать его из приюта")
                .url("https://clck.ru/34Gguo").callbackData("/firstMeetWithPet");
        InlineKeyboardButton documentsList = new InlineKeyboardButton("Список документов, необходимых для того, чтобы взять животное из приюта")
                .url("https://clck.ru/34Ggwt").callbackData("/documentsList");
        InlineKeyboardButton transportation = new InlineKeyboardButton("Список рекомендаций по транспортировке животного")
                .url("https://clck.ru/34Ggyv").callbackData("/transportation");
        InlineKeyboardButton gettingPuppyRules = new InlineKeyboardButton("Список рекомендаций по обустройству дома для щенка")
                .url("https://clck.ru/34Gh34").callbackData("/gettingPuppyRules");
        InlineKeyboardButton gettingKittyRules = new InlineKeyboardButton("Список рекомендаций по обустройству дома для котёнка")
                .url("https://clck.ru/34Gh4G").callbackData("/gettingKittyRules");
        InlineKeyboardMarkup recommendationsKeyboard = new InlineKeyboardMarkup(safetyRules, firstMeetWithPet, documentsList,
                transportation, gettingPuppyRules, gettingKittyRules);

        this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(), """
        Вот перечень рекомендаций, которые могут пригодиться. Чтобы получить интересующую, 
        отправь боту номер этой рекомендации.""").replyMarkup(recommendationsKeyboard));

        if (shelterRepository.existsById(1L)) {
            recommendationRepository.
                    findRecommendationsByPetType_Id(1).stream()
                    .map(r -> r.getRecommendationName())
                    .forEach(System.out::println);
            recommendationRepository.findRecommendationsByPetType_Id(3);
        } else if (shelterRepository.existsById(2L)) {
            recommendationRepository.findRecommendationsByPetType_Id(2);
            recommendationRepository.findRecommendationsByPetType_Id(3);
        }
    }
}
