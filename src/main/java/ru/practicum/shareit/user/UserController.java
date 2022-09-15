package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> findAllUsers() {
        log.debug("Total number of users: {}", userService.findAllUsers().size());
        return userService.findAllUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {
        log.info("Get user by id={}", userId);
        User user = userService.get(userId);
        return userMapper.toUserDto(user);
    }

    @PostMapping
    public UserDto createUser(@Validated({Create.class}) @RequestBody UserDto userDto) {
        User user = userService.saveUser(userDto);
        log.debug("Number of added users: {}", 1);
        return userMapper.toUserDto(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @PathVariable("userId") long userId, @RequestBody UserDto userDto) {
        User user = userService.updateUser(userId, userDto);
        log.debug("User updated");
        return userMapper.toUserDto(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long userId) {
        userService.deleteUserById(userId);
        log.debug("User with id= {} deleted", userId);
    }


}
