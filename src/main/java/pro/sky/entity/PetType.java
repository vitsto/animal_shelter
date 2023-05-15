package pro.sky.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pet_type")
@Getter
@Setter
public class PetType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name", nullable = false)
    private String typeName;
    @OneToMany(mappedBy = "petType")
    private Set<Recommendation> recommendationSet = new HashSet<>();
}
