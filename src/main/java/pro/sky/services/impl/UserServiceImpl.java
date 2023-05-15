package pro.sky.services.impl;

import org.springframework.stereotype.Service;
import pro.sky.entity.User;
import pro.sky.repository.UserRepository;
import pro.sky.services.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void writeContact(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }
}
