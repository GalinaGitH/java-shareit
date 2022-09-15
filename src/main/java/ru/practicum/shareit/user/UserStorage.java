package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User update(User user);

    void removeUserById(long userId);

    User get(long userId);

    List<User> findAllUsers();
}
