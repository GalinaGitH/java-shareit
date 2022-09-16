package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.exception.AlreadyExistException;
import ru.practicum.shareit.user.exception.ValidationEmailException;

import java.util.HashSet;
import java.util.Set;

@Component
public class UniqueEmailsStorage {

    private final Set<String> uniqueEmails = new HashSet<>();

    public void checkEmailForUniquenessAndValidity(String email) {
        if (!email.contains(" ") && email.matches(".+@.+\\.[a-z]+")) {
            if (uniqueEmails.contains(email)) {
                throw new AlreadyExistException("User with this email already exist");
            } else uniqueEmails.add(email);
        } else throw new ValidationEmailException("Email is incorrect");
    }

    public void deleteEmailFromSetStorage(String email) {
        uniqueEmails.remove(email);
    }
}
