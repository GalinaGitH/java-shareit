package ru.practicum.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.item.dto.Create;
import ru.practicum.gateway.user.dto.UserRequestDto;

import javax.validation.Valid;

@Validated
@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        log.info("Get all users");
        return userClient.findAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable long userId) {
        log.info("Get user by id={}", userId);
        return userClient.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @Validated({Create.class}) @RequestBody UserRequestDto userRequestDto) {
        log.info("Creating user {}", userRequestDto);
        return userClient.createUser(userRequestDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Valid @PathVariable("userId") long userId, @RequestBody UserRequestDto userRequestDto) {
        log.info("User updated");
        return userClient.updateUser(userId, userRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") long userId) {
        log.debug("Delete user with id= {}", userId);
        return userClient.deleteUser(userId);
    }

}
