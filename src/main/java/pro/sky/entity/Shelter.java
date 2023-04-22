package pro.sky.entity;

import javax.persistence.*;
import java.util.HashSet;

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
    @JoinColumn(name = "pet_type")
    private PetType petType;
    @OneToMany(mappedBy = "shelter_id")
    private Set<Volunteer> volunteerSet = new HashSet<>();

}
