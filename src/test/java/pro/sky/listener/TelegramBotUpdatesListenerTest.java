package pro.sky.listener;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pro.sky.entity.Shelter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TelegramBotUpdatesListenerTest {
    @MockBean
    TelegramBot telegramBot;
    @Autowired
    TelegramBotUpdatesListener telegramBotUpdatesListener;

    @Test
    void handleStartTest() throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(TelegramBotUpdatesListenerTest.class.getResource("update.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", "/start"), Update.class);
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage message = (SendMessage) arg;
            Assertions.assertThat(message.getParameters().get("chat_id")).isEqualTo(update.message().chat().id());
            Assertions.assertThat(message.getParameters().get("text")).isEqualTo("""
                                        Привет! Это бот приюта для животных из Астаны.
                                        Я отвечу на твои вопросы и расскажу, как забрать животное к себе домой.\n
                                        Для начала выбери приют. Выбрать несколько нельзя.""");
            return null;
        });
        telegramBotUpdatesListener.process(List.of(update));
    }

    @Test
    void handleChooseCatShelterTest() throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(TelegramBotUpdatesListenerTest.class.getResource("update.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", "/catShelter"), Update.class);
        when(telegramBot.execute(any())).then((invocation) -> {
            Shelter shelter = TelegramBotUpdatesListener.getClientIdToShelter().get(update.message().chat().id());
            Assertions.assertThat(shelter).isNotNull();
            Assertions.assertThat(shelter.getPetType().getTypeName()).isEqualToIgnoringCase("cat");
            return null;
        });
        telegramBotUpdatesListener.process(List.of(update));
    }

    @Test
    void handleChooseDogShelterTest() throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(TelegramBotUpdatesListenerTest.class.getResource("update.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", "/dogShelter"), Update.class);
        when(telegramBot.execute(any())).then((invocation) -> {
            Shelter shelter = TelegramBotUpdatesListener.getClientIdToShelter().get(update.message().chat().id());
            Assertions.assertThat(shelter).isNotNull();
            Assertions.assertThat(shelter.getPetType().getTypeName()).isEqualToIgnoringCase("dog");
            return null;
        });
        telegramBotUpdatesListener.process(List.of(update));
    }

    @Test
    void handleChooseAboutWithoutShelterTest() throws URISyntaxException, IOException {
        String json = Files.readString(Path.of(TelegramBotUpdatesListenerTest.class.getResource("update.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", "/about"), Update.class);
        when(telegramBot.execute(any())).then((invocation) -> {
            Object arg = invocation.getArgument(0);
            SendMessage message = (SendMessage) arg;
            Assertions.assertThat(message.getParameters().get("chat_id")).isEqualTo(update.message().chat().id());
            Assertions.assertThat(message.getParameters().get("text")).isEqualTo("Приют не выбран.");
            return null;
        });
        telegramBotUpdatesListener.process(List.of(update));
    }
}