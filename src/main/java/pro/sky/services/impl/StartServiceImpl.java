package pro.sky.services.impl;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.services.StartService;

@Service
public class StartServiceImpl implements StartService {
    @Override
    public SendMessage start(Long id) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Приют для кошек").callbackData("/catShelter"),
                new InlineKeyboardButton("Приют для собак").callbackData("/dogShelter")
        );
        return new SendMessage(id, """
Привет! Это бот приюта для животных из Астаны. 
Я отвечу на твои вопросы и расскажу, как забрать животное к себе домой.\n
Для начала выбери приют. Выбрать несколько нельзя.""").replyMarkup(keyboardMarkup);
    }
}