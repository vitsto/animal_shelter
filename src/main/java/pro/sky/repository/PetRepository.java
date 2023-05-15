package pro.sky.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pro.sky.entity.Pet;

@Repository
public interface PetRepository extends CrudRepository<Pet, Long> {
}
