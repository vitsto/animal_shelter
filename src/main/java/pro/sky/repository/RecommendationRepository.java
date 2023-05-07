package pro.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.entity.Recommendation;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    @Query("FROM Recommendation WHERE petType.id = :petTypeId")
    List<Recommendation> findRecommendationsByPetType_Id(@Param("petTypeId") long petTypeId);
}
