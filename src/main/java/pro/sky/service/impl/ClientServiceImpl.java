package pro.sky.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.stereotype.Service;
import pro.sky.entity.Client;
import pro.sky.repository.ClientRepository;
import pro.sky.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

    private final TelegramBot telegramBot;

    private final ClientRepository clientRepository;

    public ClientServiceImpl(TelegramBot telegramBot, ClientRepository clientRepository) {
        this.telegramBot = telegramBot;
        this.clientRepository = clientRepository;
    }

    @Override
    public void saveClientToRepository(Client client) {
        clientRepository.save(client);
    }
}
