package pro.sky.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "shelter")
public class Shelter {
    @Id
    private Long id;
    private String name;
    private String address;
    private String schedule;
    @Column(name = "location_map")
    private String locationMap;
    @ManyToOne
    @JoinColumn(name = "pet_type_id")
    private PetType petType;
    @OneToMany(mappedBy = "shelter")
    private Set<Volunteer> volunteerSet = new HashSet<>();

}
