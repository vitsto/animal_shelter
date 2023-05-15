package pro.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.entity.Recommendation;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
