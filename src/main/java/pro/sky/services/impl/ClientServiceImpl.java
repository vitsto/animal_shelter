package pro.sky.services.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.entity.Client;
import pro.sky.repository.ClientRepository;
import pro.sky.services.ClientService;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClientServiceImpl implements ClientService {

    private final TelegramBot telegramBot;
    private final ClientRepository clientRepository;

    private final Pattern patternForClient = Pattern.compile("([А-я\\s]+)\\s+(\\d{11})\\s+([А-я\\d\\s.,]+)");

    public ClientServiceImpl(TelegramBot telegramBot, ClientRepository clientRepository) {
        this.telegramBot = telegramBot;
        this.clientRepository = clientRepository;
    }

    /**
     * Метод сохранения данных клиента в базу данных.
     *
     * @param client экземпляр класса {@link Client}.
     */
    @Override
    public Client saveClientToRepository(Client client) {
        clientRepository.save(client);
        return client;
    }

    /**
     * Метод для парсинга контактных данных пользователя.
     *
     * @param fromId идентификатор пользователя.
     * @param clientData контактные данные, переданные пользователем в бот.
     * @return client — экземпляр класса {@link Client}.
     */
    @Override
    public Client parseClientData(Long fromId, String clientData) {
        Client client = new Client();
        if (clientData != null) {
            Matcher matcher = patternForClient.matcher(clientData);
            if (matcher.find()) {
                String name = matcher.group(1);
                String phoneNumber = matcher.group(2);
                String address = matcher.group(3);
                client.setId(fromId);
                client.setName(name);
                client.setPhoneNumber(phoneNumber);
                client.setAddress(address);
                new SendMessage(fromId, "Данные успешно сохранены.");
            } else {
                new SendMessage(fromId, "Некорректный формат данных!");
            }
        }
        return client;
    }

    /**
     * Метод поиска клиента по его идентификатору.
     *
     * @param id идентификатор клиента.
     */
    @Override
    public Optional<Client> findClient(Long id) {
        return clientRepository.findById(id);
    }
}
