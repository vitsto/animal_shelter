package pro.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.entity.Volunteer;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
}
