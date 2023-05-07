package pro.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.entity.PetType;
import pro.sky.entity.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    Shelter getShelterById(long id);

    Shelter findShelterByPetTypeIs(PetType petType);
}
