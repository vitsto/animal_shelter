package pro.sky.services;

import com.pengrad.telegrambot.request.SendMessage;

public interface IncorrectMessageService {
    SendMessage shelterNotChoose(Long id);

    SendMessage commandIncorrect(Long id);
}
