package pro.sky.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для Telegram-бота.
 */
@Configuration
public class TelegramBotConfiguration {


    /**
     * Метод создания бина для взаимодействия с Telegram Bot API.
     * @param token токен Telegram-бота.
     * @return объект Telegram-бота.
     * @see <a href="https://core.telegram.org/bots/api">...</a>
     */
    @Bean
    public TelegramBot telegramBot(@Value("${telegram.bot.token}") String token) {
        return new TelegramBot(token);
    }
}
