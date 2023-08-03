package pro.sky.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.entity.User;
import pro.sky.services.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@Tag(name = "Пользователи", description = "Операции создания записей о новых пользователях и получения данных о них")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (user.getName() == null || user.getPhoneNumber() == null ||
                user.getShelter() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.writeContact(user));
    }

    @GetMapping("/get")
    public ResponseEntity<Optional<User>> getUser(@RequestParam (name = "id") Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }
}
