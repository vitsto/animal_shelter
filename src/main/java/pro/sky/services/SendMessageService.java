package pro.sky.services;

import com.pengrad.telegrambot.request.SendMessage;

public interface SendMessageService {
    SendMessage shelterNotChoose(Long id);

    SendMessage commandIncorrect(Long id);

    SendMessage send(Long id, String text);
}
