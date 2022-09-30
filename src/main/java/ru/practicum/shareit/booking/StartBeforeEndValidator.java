package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.NewBookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, NewBookingDto> {

    @Override
    public boolean isValid(NewBookingDto newBookingDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime currentDate = LocalDateTime.now();

        return (newBookingDto.getStart().isAfter(currentDate) || newBookingDto.getStart().isEqual(currentDate))
                && (newBookingDto.getEnd().isAfter(currentDate) || newBookingDto.getEnd().isEqual(currentDate))
                && (newBookingDto.getStart().isBefore(newBookingDto.getEnd()) || newBookingDto.getEnd().isEqual(currentDate));
    }
}
