package src.main.java.ru.practicum.gateway.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.gateway.booking.validator.StartBeforeEnd;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@StartBeforeEnd
public class BookItemRequestDto {
    private long itemId;
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
}
