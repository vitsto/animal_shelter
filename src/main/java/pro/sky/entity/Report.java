package pro.sky.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String photo;  //здесь будет хранится путь к фото

    public Report(Pet pet, LocalDateTime dateTime, String description, String photo) {
        this.pet = pet;
        this.dateTime = dateTime;
        this.description = description;
        this.photo = photo;
    }

    public Report() {

    }
}
