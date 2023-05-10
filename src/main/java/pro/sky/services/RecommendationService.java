package pro.sky.services;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.HashMap;

public interface RecommendationService {

    SendMessage menuForClientConsultation(Long fromId);

    SendMessage giveRecommendation(Long fromId, HashMap<Long, Long> clientIdToShelterId);

}
