package pro.sky.services;

import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.entity.Shelter;

public interface ShelterService {
    SendMessage start(Shelter shelter, Long fromId);

    SendMessage aboutShelter(Shelter shelter, Long fromId);

    SendMessage getGuardContact(Shelter shelter, Long fromId);

    SendMessage infoShelter(Shelter shelter, Long fromId);
}
