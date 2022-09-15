package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;

@Component
public class UniqueEmailsStorage {

    private final HashSet<String> uniqueEmails = new HashSet<>();

    public void checkEmailForUniquenessAndValidity(String email) {
        if (!email.contains(" ") && email.matches(".+@.+\\.[a-z]+")) {
            if (uniqueEmails.contains(email)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            } else uniqueEmails.add(email);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public void deleteEmailFromSetStorage(String email) {
        uniqueEmails.remove(email);
    }
}
