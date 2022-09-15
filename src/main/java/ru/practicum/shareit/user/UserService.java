package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    UserDto get(long userId);

    List<UserDto> findAllUsers();

    void deleteUserById(long userId);
}
