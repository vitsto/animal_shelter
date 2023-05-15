package pro.sky.services;

import pro.sky.entity.Client;

import java.util.Optional;

public interface ClientService {

    Client saveClientToRepository(Client client);

    Client parseClientData(Long fromId, String clientData);

    Optional<Client> findClient(Long id);
}
