package pro.sky.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.entity.PetType;
import pro.sky.entity.Shelter;
import pro.sky.repository.PetTypeRepository;
import pro.sky.repository.ShelterRepository;

@SpringBootTest
public class ShelterServiceTest {
    @Autowired
    ShelterRepository shelterRepository;
    @Autowired
    PetTypeRepository petTypeRepository;

    @Test
    void aboutDogShelter() {
        PetType petType = petTypeRepository.findPetTypeByTypeNameIgnoreCase("dog");
        Assertions.assertThat(petType).isNotNull();
        Shelter shelter = shelterRepository.findShelterByPetTypeIs(petType);
        Assertions.assertThat(shelter.getAbout()).isNotNull();
    }

    @Test
    void aboutCatShelter() {
        PetType petType = petTypeRepository.findPetTypeByTypeNameIgnoreCase("cat");
        Assertions.assertThat(petType).isNotNull();
        Shelter shelter = shelterRepository.findShelterByPetTypeIs(petType);
        Assertions.assertThat(shelter.getAbout()).isNotNull();
    }
}
