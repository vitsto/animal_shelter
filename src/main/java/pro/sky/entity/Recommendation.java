package pro.sky.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "recommendation")
@Getter
@Setter
@NoArgsConstructor
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
