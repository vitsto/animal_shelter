package pro.sky.entity;

import javax.persistence.*;

@Entity
@Table(name = "recommendation")
public class Recommendation {
    @Id
    private Long id;
    @Column(name = "recommendation_name", nullable = false)
    private String recommendationName;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "pet_type_id")
    private PetType petType;
}
