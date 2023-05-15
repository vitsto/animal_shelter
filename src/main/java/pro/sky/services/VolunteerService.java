package pro.sky.services;

import pro.sky.entity.Volunteer;

import java.util.Optional;

public interface VolunteerService {

    void callVolunteer();

    Volunteer saveVolunteerToRepository(Volunteer volunteer);

    Optional<Volunteer> findVolunteer(Long id);
}
