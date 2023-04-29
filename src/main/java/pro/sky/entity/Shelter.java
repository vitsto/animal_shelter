package pro.sky.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shelter")
public class Shelter {
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String schedule;
    @Column(nullable = false)
    private String about;
    @Column(name = "location_map")
    private String locationMap;
    @ManyToOne
    @JoinColumn(name = "pet_type_id")
    private PetType petType;
    @OneToMany(mappedBy = "shelter")
    private Set<Volunteer> volunteerSet = new HashSet<>();

}
