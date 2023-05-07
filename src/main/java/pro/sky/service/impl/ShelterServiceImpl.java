package pro.sky.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.stereotype.Service;
import pro.sky.entity.PetType;
import pro.sky.entity.Shelter;
import pro.sky.repository.PetTypeRepository;
import pro.sky.repository.ShelterRepository;
import pro.sky.service.ShelterService;

import java.util.Optional;

@Service
public class ShelterServiceImpl implements ShelterService {

    private final TelegramBot telegramBot;

    private final ShelterRepository shelterRepository;

    private final PetTypeRepository petTypeRepository;

    public ShelterServiceImpl(TelegramBot telegramBot, ShelterRepository shelterRepository, PetTypeRepository petTypeRepository) {
        this.telegramBot = telegramBot;
        this.shelterRepository = shelterRepository;
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    public Shelter chooseShelter(String type) {
        Shelter shelter = null;
        Optional<PetType> petType = Optional.ofNullable(petTypeRepository.findPetTypeByTypeName(type));
        if (petType.isPresent()) {
            shelter = shelterRepository.findShelterByPetTypeIs(petType.get());
        }
        return shelter;
    }
}
