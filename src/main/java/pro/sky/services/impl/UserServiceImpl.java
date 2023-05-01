package pro.sky.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.entity.User;
import pro.sky.repository.UserRepository;
import pro.sky.services.UserService;
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
}
