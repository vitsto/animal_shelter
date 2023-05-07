package pro.sky.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "pet_type")
@Getter
public class PetType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name")
    private String typeName;
}
