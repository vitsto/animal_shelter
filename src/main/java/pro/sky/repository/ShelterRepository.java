package pro.sky.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pro.sky.entity.PetType;
import pro.sky.entity.Shelter;

import java.util.List;

@Repository
public interface ShelterRepository extends CrudRepository<Shelter, Long> {
    Shelter findShelterByPetTypeIs(PetType petType);
}
