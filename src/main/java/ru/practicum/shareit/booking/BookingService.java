package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(long userId, NewBookingDto newBookingDto);

    BookingDto changeBooking(long userId, Long bookingId, boolean approved);

    BookingDto getBooking(long userId, Long bookingId);

    List<BookingDto> getBookingOfUser(long userId, BookingState state,int from,int size);

    List<BookingDto> getBookingOfOwner(long userId, BookingState state,int from,int size);

}
