package pro.sky.services;

import pro.sky.entity.User;

import java.util.Optional;

public interface UserService {
    void writeContact(User user);

    Optional<User> getUser(Long id);
}
