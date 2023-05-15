package pro.sky.services.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Service;
import pro.sky.entity.Volunteer;
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
     * Метод вызова волонтёра.
     */
    @Override
    public void callVolunteer() {
        InlineKeyboardMarkup buttonToCallVolunteer = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Позвать волонтёра").callbackData("/callVolunteer"));

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
