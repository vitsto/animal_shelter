package pro.sky.service;

import com.pengrad.telegrambot.model.Update;
import pro.sky.entity.Shelter;

import java.util.HashMap;

public interface RecommendationService {
    void giveRecommendation(Update update, HashMap<Long, Long> clientIdToShelterId);
}
