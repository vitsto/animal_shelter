package pro.sky.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
    @Id
    private Long id;
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
}
