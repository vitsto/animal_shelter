package pro.sky.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
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
     * Метод первичной обработки поступивших в бот апдейтов.
     * @param updates список всех поступивших апдейтов.
     * @return количество обработанных апдейтов.
     */
    @Override
    public int process(List<Update> updates) {
            updates.stream()
                    .filter(update -> update.message() != null || update.callbackQuery() != null)
                    .forEach(this::handleUpdate);

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Метод обрабатывает апдейт /start и дает пользователю возможность выбрать приют.
     * @param update сообщение, поступившее в бот.
     */
    private void handleUpdate(Update update) {
        try {
        logger.info("Обработан update: " + update);

            if (update.message() != null && update.message().text().startsWith("/start")) {
                    InlineKeyboardButton catShelter = new InlineKeyboardButton("Приют для кошек").callbackData("/catShelter");
                    InlineKeyboardButton dogShelter = new InlineKeyboardButton("Приют для собак").callbackData("/dogShelter");
                    InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(catShelter, dogShelter);
                    this.telegramBot.execute(new SendMessage(update.message().chat().id(), """
                            Привет! Это бот приюта для животных из Астаны. 
                            Я отвечу на твои вопросы и расскажу, как забрать животное к себе домой.\n
                            Для начала выбери приют. Выбрать оба нельзя.""").replyMarkup(keyboard));
                    return;
                }
            if (update.callbackQuery().data().startsWith("/catShelter")) {
                this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(), "Выбран приют для кошек"));
            } else {
                this.telegramBot.execute(new SendMessage(update.callbackQuery().from().id(), "Выбран приют для собак"));
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
