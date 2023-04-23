package pro.sky.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    private String description;

    private String photo;  //здесь будет хранится путь к фото

}
