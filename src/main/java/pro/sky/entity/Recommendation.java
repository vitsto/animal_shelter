package pro.sky.entity;

import javax.persistence.*;

@Entity
@Table(name = "recommendation")
public class Recommendation {
    @Id
    private Long id;
    @Column(name = "recommendation_name")
    private String recommendationName;
    @ManyToOne
    @JoinColumn(name = "pet_type")
    private PetType petType;
}
