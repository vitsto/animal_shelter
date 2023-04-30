package pro.sky.entity;

import javax.persistence.*;

@Entity
@Table(name = "recommendation")
public class Recommendation {
    @Id
    private Long id;
    @Column(name = "recommendation_name")
    private String recommendationName;
    private String description;
    @ManyToOne
    @JoinColumn(name = "pet_type_id")
    private PetType petType;

    public Recommendation(Long id, String recommendationName, String description, PetType petType) {
        this.id = id;
        this.recommendationName = recommendationName;
        this.description = description;
        this.petType = petType;
    }

    public Recommendation() {
    }

    public Long getId() {
        return id;
    }

    public String getRecommendationName() {
        return recommendationName;
    }

    public void setRecommendationName(String recommendationName) {
        this.recommendationName = recommendationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    @Override
    public String toString() {
        return id + " " + recommendationName;
    }
}
