package ru.practicum.gateway.booking.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

import src.main.java.ru.practicum.gateway.booking.dto.BookItemRequestDto;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookItemRequestDto> {

    @Override
    public boolean isValid(BookItemRequestDto dto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime currentDate = LocalDateTime.now();

        return (dto.getStart().isAfter(currentDate) || dto.getStart().isEqual(currentDate))
                && (dto.getEnd().isAfter(currentDate) || dto.getEnd().isEqual(currentDate))
                && (dto.getStart().isBefore(dto.getEnd()) || dto.getEnd().isEqual(currentDate));
    }
}
