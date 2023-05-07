package pro.sky.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Service;
import pro.sky.repository.VolunteerRepository;
import pro.sky.service.VolunteerService;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final TelegramBot telegramBot;
    private final VolunteerRepository volunteerRepository;

    public VolunteerServiceImpl(TelegramBot telegramBot, VolunteerRepository volunteerRepository) {
        this.telegramBot = telegramBot;
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public void callVolunteer() {
        InlineKeyboardButton callVolunteer = new InlineKeyboardButton("Позвать волонтёра").callbackData("/callVolunteer");
        InlineKeyboardMarkup buttonToCallVolunteer = new InlineKeyboardMarkup(callVolunteer);

    }
}
