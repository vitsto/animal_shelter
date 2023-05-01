package pro.sky.entity;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    public User(String name, String phoneNumber, Shelter shelter) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.shelter = shelter;
    }
}
