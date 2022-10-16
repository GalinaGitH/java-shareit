package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {
    UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    private User user;

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(userRepository, userMapper);
        user = new User(1L, "user 1", "user1@email");
    }

    @Test
    void saveUserTest() {
        UserDto dto = new UserDto(1L, "user 1", "user1@email");
        when(userRepository.save(user))
                .thenReturn(user);
        userService.saveUser(dto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserDtoTest() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        userService.get(1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findAllUsersTest() {
        UserDto dto = new UserDto(1L, "user 1", "user1@email");
        userService.saveUser(dto);
        final List<User> allUsers = new ArrayList<>(Collections.singletonList(user));
        when(userRepository.findAll())
                .thenReturn(allUsers);

        final List<UserDto> userDtos = userService.findAllUsers();

        assertNotNull(userDtos);
        assertEquals(1, userDtos.size());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateAndDeleteUserByIdTest() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        UserDto dto = new UserDto(1L, "user update", "user1@email");
        userService.updateUser(1L, dto);
        userService.deleteUserById(1L);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void getUserWithExceptionTest() {
        when(userRepository.findById(1L))
                .thenThrow(new NotFoundException());
        final var ex = assertThrows(RuntimeException.class, () -> userService.get(1L));
        verify(userRepository, times(1)).findById(1L);
    }
}
