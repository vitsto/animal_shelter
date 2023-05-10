package pro.sky.services;

import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.entity.Shelter;

import java.util.HashMap;

public interface ShelterService {
    Shelter chooseShelter(String type);

    SendMessage giveMenu(Long fromId);

    SendMessage start(Shelter shelter, Long fromId);

    SendMessage aboutShelter(Shelter shelter, Long fromId);

    SendMessage getGuardContact(Shelter shelter, Long fromId);

    SendMessage infoShelter(Shelter shelter, Long fromId);
}
