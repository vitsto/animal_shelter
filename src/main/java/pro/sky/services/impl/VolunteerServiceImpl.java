package pro.sky.services.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.entity.Shelter;
import pro.sky.entity.Volunteer;
import pro.sky.exception.VolunteerNotFoundException;
import pro.sky.repository.VolunteerRepository;
import pro.sky.services.VolunteerService;
import java.util.Optional;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final TelegramBot telegramBot;
    private final VolunteerRepository volunteerRepository;

    public VolunteerServiceImpl(TelegramBot telegramBot,
                                VolunteerRepository volunteerRepository) {
        this.telegramBot = telegramBot;
        this.volunteerRepository = volunteerRepository;
    }

    /**
     * Метод вызова волонтёра
     *
     * @param fromId идентификатор пользователя.
     * @param shelter приют, выбранный пользователем.
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage callVolunteer(Long fromId, Shelter shelter) {
        String nickname = volunteerRepository.findAllByShelter_Id(shelter.getId()).stream()
                .map(v -> v.getTelegramNickname())
                .findAny()
                .orElseThrow(() -> new VolunteerNotFoundException("Волонтёр не найден"));

        return new SendMessage(fromId, "Вы можете связаться с волонтёром в Telegram: " + nickname);
    }

    /**
     * Метод сохранения данных волонтёра в базу данных.
     *
     * @param volunteer экземпляр класса {@link Volunteer}.
     */
    @Override
    public Volunteer saveVolunteerToRepository(Volunteer volunteer) {
        volunteerRepository.save(volunteer);
        return volunteer;
    }

    /**
     * Метод поиска волонтёра по его идентификатору.
     *
     * @param id идентификатор волонтёра.
     */
    @Override
    public Optional<Volunteer> findVolunteer(Long id) {
        return volunteerRepository.findById(id);
    }
}
