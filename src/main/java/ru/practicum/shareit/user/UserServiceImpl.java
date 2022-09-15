package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UniqueEmailsStorage uniqueEmailsStorage;

    /**
     * сохранение пользователя
     */
    @Override
    public User saveUser(UserDto userDto) {
        uniqueEmailsStorage.checkEmailForUniquenessAndValidity(userDto.getEmail());//проверка email на уникальность и валидность
        User user = UserMapper.toUser(userDto);
        return userStorage.createUser(user);
    }

    /**
     * изменение пользователя
     */
    @Override
    public User updateUser(long userId, UserDto userDto) {
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
        return userInStorage;
    }

    /**
     * получение пользователя по id
     */
    @Override
    public User get(long userId) {
        final User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException("User with id=" + userId + "not found");
        }
        return user;
    }

    /**
     * получение списка всех пользователей
     */
    @Override
    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
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
