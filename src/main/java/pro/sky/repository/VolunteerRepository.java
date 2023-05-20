package pro.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.entity.Volunteer;

import java.util.List;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    List<Volunteer> findAllByShelter_Id(Long shelterId);
}
