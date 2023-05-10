package pro.sky.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "volunteer")
@Getter
@Setter
@NoArgsConstructor
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @OneToMany(mappedBy = "volunteer")
    private Set<Pet> petSet = new HashSet<>();
}
