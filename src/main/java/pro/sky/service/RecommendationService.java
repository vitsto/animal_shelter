package pro.sky.service;

import com.pengrad.telegrambot.model.Update;

public interface RecommendationService {
    void giveRecommendation(Update update);

}
