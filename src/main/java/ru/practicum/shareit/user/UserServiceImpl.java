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
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * сохранение пользователя
     */
    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    /**
     * изменение пользователя
     */
    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        final User userInStorage = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        if (userDto.getName() != null) {
            userInStorage.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userInStorage.setEmail(userDto.getEmail());
        }
        userRepository.save(userInStorage);
        return userMapper.toUserDto(userInStorage);
    }

    /**
     * получение пользователя по id
     */
    @Override
    public UserDto get(long userId) {
        final User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        return userMapper.toUserDto(user);
    }

    /**
     * получение списка всех пользователей
     */
    @Override
    public List<UserDto> findAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .map((User user) -> userMapper.toUserDto(user))
                .collect(Collectors.toList());
    }

    /**
     * удаление пользователя
     */
    @Override
    public void deleteUserById(long userId) {
        final User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        userRepository.delete(user);
    }
}
