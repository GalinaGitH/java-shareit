package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto bookItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody NewBookingDto newBookingDto) {
        BookingDto bookingDtoCreated = bookingService.createBooking(userId, newBookingDto);
        log.debug("Booking created");
        return bookingDtoCreated;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeOfBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable("bookingId") long bookingId,
                                      @RequestParam("approved") Boolean approved) {
        BookingDto bookingDto = bookingService.changeBooking(userId, bookingId, approved);
        log.debug("Booking request changed");
        return bookingDto;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("bookingId") long bookingId) {
        BookingDto bookingDto = bookingService.getBooking(userId, bookingId);
        log.debug("Booking data received");
        return bookingDto;
    }

    @GetMapping
    public List<BookingDto> getBookingOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                             @RequestParam(name = "from", defaultValue = "0") int from,
                                             @RequestParam(name = "size", defaultValue = "10") int size) {
        BookingState state = BookingState.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + stateParam);
        }
        List<BookingDto> bookings = bookingService.getBookingOfUser(userId, state, from, size);
        log.info("Get List of bookings of user");
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingOfOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                              @RequestParam(name = "from", defaultValue = "0") int from,
                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        BookingState state = BookingState.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + stateParam);
        }
        List<BookingDto> bookings = bookingService.getBookingOfOwner(userId, state, from, size);
        log.info("Get List of bookings of user");
        return bookings;
    }

}
