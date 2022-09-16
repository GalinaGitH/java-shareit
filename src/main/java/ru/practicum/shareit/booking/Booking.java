package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Booking {

    @NotNull
    private long id;
    private LocalDateTime start;//дата и время начала бронирования;
    private LocalDateTime end;//дата и время конца бронирования;
    private Item item;//вещь, которую пользователь бронирует;
    private User booker;//пользователь, который осуществляет бронирование;
    private BookingStatus status;//статус бронирования.
}
