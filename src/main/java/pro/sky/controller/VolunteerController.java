package pro.sky.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.entity.Client;
import pro.sky.entity.Volunteer;
import pro.sky.services.VolunteerService;

import java.util.Optional;


@RestController
@RequestMapping("/volunteer")
@Tag(name = "Волонтёры", description = "Операции создания записей о новых волонтёрах и получения данных о них")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping("/add")
    public ResponseEntity<Volunteer> addVolunteer(@RequestBody Volunteer volunteer) {
        if (volunteer.getName() == null || volunteer.getShelter() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(volunteerService.saveVolunteerToRepository(volunteer));
    }

    @GetMapping("/get")
    public ResponseEntity<Optional<Volunteer>> getVolunteer(@RequestParam(name = "id") Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Volunteer> volunteer = volunteerService.findVolunteer(id);
        return ResponseEntity.ok(volunteer);
    }
}
