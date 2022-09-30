package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.exception.BadRequestException;
import ru.practicum.shareit.booking.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.booking.exception.NotOwnerException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;


    /**
     * Добавление бронирования/аренда
     * После создания запрос находится в статусе WAITING — «ожидает подтверждения»
     */
    @Override
    @Transactional
    public BookingDto createBooking(long userId, NewBookingDto newBookingDto) {
        final Item itemInStorage = itemRepository.findById(newBookingDto.getItemId())
                .orElseThrow(NotFoundException::new);
        if (!itemInStorage.getAvailable()) {
            throw new ItemIsNotAvailableException("item not available for booking");
        }
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        if (itemInStorage.getOwner().getId().equals(user.getId())) {
            throw new NotFoundException();//владелец не может бронировать свои же вещи
        }
        Booking booking = bookingMapper.toBooking(newBookingDto, itemInStorage, user);
        booking.setBooker(user);
        booking.setItem(itemInStorage);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(savedBooking);
    }

    /**
     * Подтверждение или отклонение запроса на бронирование.
     * Может быть выполнено только владельцем вещи
     */
    @Override
    @Transactional
    public BookingDto changeBooking(long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(NotFoundException::new);
        User owner = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        if (!booking.getItem().getOwner().getId().equals(owner.getId())) {
            throw new NotOwnerException("the request can only change by the owner");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new BadRequestException("Status already APPROVED");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking changedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(changedBooking);
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус).
     * Может быть выполнено либо автором бронирования,
     * либо владельцем вещи, к которой относится бронирование.
     */
    @Override
    public BookingDto getBooking(long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(NotFoundException::new);
        User owner = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        if (!booking.getItem().getOwner().getId().equals(owner.getId()) && !booking.getBooker().getId().equals(owner.getId())) {
            throw new NotOwnerException("the request can only be made by the owner or booker");
        }
        return bookingMapper.toBookingDto(booking);
    }

    /**
     * Получение списка всех бронирований текущего пользователя.
     * Сортировка бронирований по дате от более новых к более старым
     */
    @Override
    public List<BookingDto> getBookingOfUser(long userId, BookingState state) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        switch (state) {
            case ALL:
                List<Booking> bookings = bookingRepository.findAllBookingsOfUser(user.getId());
                return bookingMapper.toBookingDtoList(bookings);
            case CURRENT:
                bookings = bookingRepository.findBookingsOfUserBetween(user.getId(), LocalDateTime.now(), LocalDateTime.now());
                return bookingMapper.toBookingDtoList(bookings);
            case PAST:
                bookings = bookingRepository.findBookingsOfUserPast(user.getId(), LocalDateTime.now());
                return bookingMapper.toBookingDtoList(bookings);
            case FUTURE:
                bookings = bookingRepository.findBookingsOfUserFuture(user.getId(), LocalDateTime.now());
                return bookingMapper.toBookingDtoList(bookings);
            case WAITING:
                bookings = bookingRepository.findAllBookingsOfUserWithStatus(user.getId(), BookingStatus.WAITING);
                return bookingMapper.toBookingDtoList(bookings);
            case REJECTED:
                bookings = bookingRepository.findAllBookingsOfUserWithStatus(user.getId(), BookingStatus.REJECTED);
                return bookingMapper.toBookingDtoList(bookings);

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя-владельца вещи
     */
    @Override
    public List<BookingDto> getBookingOfOwner(long userId, BookingState state) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        List<Item> items = itemRepository.findAll().stream()
                .filter(x -> x.getOwner() == user)
                .collect(Collectors.toList());
        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        switch (state) {
            case ALL:
                List<Booking> bookings = bookingRepository.findAllBookingsOfItemsUser(itemIds);
                return bookingMapper.toBookingDtoList(bookings);
            case CURRENT:
                bookings = bookingRepository.findBookingsOfUserItemsBetween(itemIds, LocalDateTime.now(), LocalDateTime.now());
                return bookingMapper.toBookingDtoList(bookings);
            case PAST:
                bookings = bookingRepository.findBookingsOfUserItemsInPast(itemIds, LocalDateTime.now());
                return bookingMapper.toBookingDtoList(bookings);
            case FUTURE:
                bookings = bookingRepository.findBookingsOfUserItemsInFuture(itemIds, LocalDateTime.now());
                return bookingMapper.toBookingDtoList(bookings);
            case WAITING:
                bookings = bookingRepository.findAllBookingsOfUserItemsWithStatus(itemIds, BookingStatus.WAITING);
                return bookingMapper.toBookingDtoList(bookings);
            case REJECTED:
                bookings = bookingRepository.findAllBookingsOfUserItemsWithStatus(itemIds, BookingStatus.REJECTED);
                return bookingMapper.toBookingDtoList(bookings);
            default:
                throw new IllegalArgumentException();
        }
    }
}
