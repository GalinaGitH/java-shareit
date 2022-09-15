package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User saveUser(UserDto userDto);

    User updateUser(long userId, UserDto userDto);

    User get(long userId);

    List<User> findAllUsers();

    void deleteUserById(long userId);
}
