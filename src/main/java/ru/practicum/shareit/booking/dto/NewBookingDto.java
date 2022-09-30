package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.StartBeforeEnd;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@StartBeforeEnd
public class NewBookingDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private long itemId;
}
