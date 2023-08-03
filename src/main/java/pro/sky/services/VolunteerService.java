package pro.sky.services;

import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.entity.Shelter;
import pro.sky.entity.Volunteer;

import java.util.Optional;

public interface VolunteerService {

    SendMessage callVolunteer(Long fromId, Shelter shelter);

    Volunteer saveVolunteerToRepository(Volunteer volunteer);

    Optional<Volunteer> findVolunteer(Long id);
}
