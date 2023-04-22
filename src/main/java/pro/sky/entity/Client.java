package pro.sky.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;

@Entity
@Table(name = "client")
public class Client {
    @Id
    private Long id;
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String address;
    @OneToMany(mappedBy = "client_id")
    private Set<Pet> petSet = new HashSet<>();
}
