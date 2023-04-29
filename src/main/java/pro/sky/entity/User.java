package pro.sky.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
}
