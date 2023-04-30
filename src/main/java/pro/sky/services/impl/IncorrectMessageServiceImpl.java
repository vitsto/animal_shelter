package pro.sky.services.impl;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.services.IncorrectMessageService;

public class IncorrectMessageServiceImpl implements IncorrectMessageService {
    /**
     * Вывод предупреждения, при попытке совершить действие над приютом, не выбрав его.
     * @param id идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage shelterNotChoose(Long id) {
        return new SendMessage(id, "Приют не выбран.").replyMarkup(
                new InlineKeyboardMarkup(
                        new InlineKeyboardButton("Выбор приюта").callbackData("/start")
                )
        );
    }

    /**
     * Вывод сообщения при получении неизвестной команды.
     * @param id идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage commandIncorrect(Long id) {
        return new SendMessage(id, "Извините, я вас не понимаю.");
    }
}
