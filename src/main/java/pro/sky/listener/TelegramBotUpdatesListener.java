package pro.sky.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Класс для обработки сообщений (апдейтов), поступающих от пользователей в Telegram-бот.
 */
@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    /**
     * Переменная интерфейса Logger для обеспечения логирования событий, происходящих в коде.
     */
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Метод обрабатывает апдейт /start и дает пользователю возможность выбрать приют.
     * @param updates список всех поступивших апдейтов.
     * @return количество обработанных апдейтов.
     */
    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Обработан update: " + update);

                Message message = update.message();
                Long chatId = message.chat().id();
                String text = message.text();

                if (update.message() != null && update.message().text().startsWith("/start")) {
                    InlineKeyboardButton catShelter = new InlineKeyboardButton("Приют для кошек").callbackData("/catShelter");
                    InlineKeyboardButton dogShelter = new InlineKeyboardButton("Приют для собак").callbackData("/dogShelter");
                    InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(catShelter, dogShelter);
                    this.telegramBot.execute(new SendMessage(chatId, """
                            Привет! Это бот приюта для животных из Астаны. 
                            Я отвечу на твои вопросы и расскажу, как забрать животное к себе домой.\n
                            Для начала выбери приют. Выбрать оба нельзя. \n
                            Чтобы увидеть клавиатуру, отправь /keyboard боту.""").replyMarkup(keyboard));
                } else {
                        sendMessage(chatId, "Некорректный формат сообщения!");
                }
                if (update.callbackQuery().data().startsWith("/catShelter")) {
                    this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(), "Выбран приют для кошек"));
                } else {
                    this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(), "Выбран приют для собак"));
                }
                });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Вспомогательный метод для отправки сообщений пользователю.
     * Используется в методе {@link #process(List)}
     * @param chatId id чата пользователя.
     * @param message текст отправляемого ботом сообщения.
     */
    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Ошибка отправки сообщения: " + sendResponse.description());
        }
    }
}
