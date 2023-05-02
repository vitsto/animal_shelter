package pro.sky.services;

import com.pengrad.telegrambot.request.SendMessage;

public interface StartService {
    SendMessage start(Long id);
}
