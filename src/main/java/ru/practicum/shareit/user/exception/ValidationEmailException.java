package ru.practicum.shareit.user.exception;


public class ValidationEmailException extends RuntimeException {
    public ValidationEmailException(String message) {
        super(message);
    }
}

