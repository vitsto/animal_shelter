package pro.sky.services.impl;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.entity.Shelter;
import pro.sky.services.ShelterService;

import java.util.Optional;

/**
 * Метод для получения информации о приюте.
 */
public class ShelterServiceImpl implements ShelterService {
    /**
     * Стартовый метод для работы с приютом.
     * @param shelter приют.
     * @param fromId идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage start(Shelter shelter, Long fromId) {
        String mess = String.format(" * Тип приюта: %s * \nВыберите действие:", shelter.getPetType().getTypeName());
        SendMessage sendMessage = new SendMessage(fromId, mess);
        sendMessage.parseMode(ParseMode.Markdown)
                .replyMarkup(getKeyboard());
        return sendMessage;
    }

    /**
     * Вывод информации о приюте.
     * @param shelter приют.
     * @param fromId идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage aboutShelter(Shelter shelter, Long fromId) {
        return new SendMessage(fromId, shelter.getAbout()).replyMarkup(getKeyboard());
    }

    /**
     * Получение контактных данных охраны.
     * @param shelter приют.
     * @param fromId идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage getGuardContact(Shelter shelter, Long fromId) {
        Optional<String> guardContact = Optional.ofNullable(shelter.getGuard());
        String mess = guardContact.orElse("Нет поста охраны.");
        return new SendMessage(fromId, mess).replyMarkup(getKeyboard());
    }

    /**
     * Вывод общей информации о приюте: адрес, расписание работы, схема проезда.
     * @param shelter приют.
     * @param fromId идентификатор пользователя.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage infoShelter(Shelter shelter, Long fromId) {
        String info = String.format("<strong>Вы можете найти нас по адресу:</strong> \n%s\n<strong>Наш график работы:</strong>\n%s\n<strong>Схема проезда: </strong>%s",
                shelter.getAddress(), shelter.getSchedule(), shelter.getLocationMap());
        return new SendMessage(fromId, info).parseMode(ParseMode.HTML).replyMarkup(getKeyboard());
    }

    /**
     * Клавиатура.
     * @return {@link Keyboard}
     */
    private Keyboard getKeyboard() {
        return new InlineKeyboardMarkup().addRow(
                new InlineKeyboardButton("Рассказать о приюте").callbackData("/about"),
                new InlineKeyboardButton("Как к нам попасть").callbackData("/info")
        ).addRow(
                new InlineKeyboardButton("Контактные данные охраны").callbackData("/guard"),
                new InlineKeyboardButton("Техника безопасности").callbackData("/safety")
        ).addRow(
                new InlineKeyboardButton("Свяжитесь со мной").callbackData("/contact"),
                new InlineKeyboardButton("Позвать волонтёра").callbackData("/volunteer")
        );
    }

}
