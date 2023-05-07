package pro.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.entity.PetType;

public interface PetTypeRepository extends JpaRepository<PetType, Long> {

    PetType findPetTypeByTypeName(String s);
}
