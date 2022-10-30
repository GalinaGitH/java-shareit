package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime start;//дата и время начала бронирования;

    @Column(name = "end_date")
    private LocalDateTime end;//дата и время конца бронирования;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;//вещь, которую пользователь бронирует;

    @JoinColumn(name = "booker_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User booker;//пользователь, который осуществляет бронирование;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;//статус бронирования.

}
