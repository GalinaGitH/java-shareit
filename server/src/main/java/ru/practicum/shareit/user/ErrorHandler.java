package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.user.exception.NotFoundException;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({NullPointerException.class,
            NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(Exception e) {
        log.info("Error 404 {}", e.getMessage());
        return Map.of("Error 404", e.getMessage());
    }

    @ExceptionHandler({MissingRequestHeaderException.class,
            MethodArgumentNotValidException.class,
            ItemIsNotAvailableException.class,
            IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(Exception e) {
        log.info(e.getMessage());
        return Map.of("error", e.getMessage());
    }

}
