package pro.sky.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pro.sky.entity.PetType;

@Repository
public interface PetTypeRepository extends CrudRepository<PetType, Long> {
    PetType findPetTypeByTypeNameIgnoreCase(String s);
}
