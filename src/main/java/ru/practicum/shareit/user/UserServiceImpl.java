package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UniqueEmailsStorage uniqueEmailsStorage;
    private final UserMapper userMapper;

    /**
     * сохранение пользователя
     */
    @Override
    public UserDto saveUser(UserDto userDto) {
        uniqueEmailsStorage.checkEmailForUniquenessAndValidity(userDto.getEmail());//проверка email на уникальность и валидность
        User user = UserMapper.toUser(userDto);
        User savedUser = userStorage.createUser(user);
        return userMapper.toUserDto(savedUser);
    }

    /**
     * изменение пользователя
     */
    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        final User userInStorage = userStorage.get(userId);
        if (userDto.getName() != null) {
            userInStorage.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            uniqueEmailsStorage.checkEmailForUniquenessAndValidity(userDto.getEmail());
            uniqueEmailsStorage.deleteEmailFromSetStorage(userInStorage.getEmail());
            userInStorage.setEmail(userDto.getEmail());
        }
        userStorage.update(userInStorage);
        return userMapper.toUserDto(userInStorage);
    }

    /**
     * получение пользователя по id
     */
    @Override
    public UserDto get(long userId) {
        final User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException("User with id=" + userId + "not found");
        }
        return userMapper.toUserDto(user);
    }

    /**
     * получение списка всех пользователей
     */
    @Override
    public List<UserDto> findAllUsers() {
        List<User> allUsers = userStorage.findAllUsers();
        return allUsers.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    /**
     * удаление пользователя
     */
    @Override
    public void deleteUserById(long userId) {
        final User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException("User with id=" + userId + "not found");
        }
        userStorage.removeUserById(userId);
        uniqueEmailsStorage.deleteEmailFromSetStorage(user.getEmail());
    }
}
