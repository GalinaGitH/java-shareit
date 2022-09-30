package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingMapper {
    public BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new BookingDto.Item(booking.getItem().getId(), booking.getItem().getName()),
                new BookingDto.Booker(booking.getBooker().getId()),
                booking.getStatus()
        );
    }

    public Booking toBooking(NewBookingDto newBookingDto, Item item, User user) {
        return new Booking(
                null,
                newBookingDto.getStart(),
                newBookingDto.getEnd(),
                item,
                user,
                BookingStatus.WAITING
        );
    }

    public List<BookingDto> toBookingDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toBookingDto)
                .collect(Collectors.toList());
    }
}
