package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Получение списка всех бронирований текущего пользователя.
     */
    @Query("SELECT b FROM Booking b WHERE b.booker.id =?1 ORDER BY b.start DESC")
    List<Booking> findAllBookingsOfUser(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.start<?2 AND b.end>?3 ORDER BY b.start DESC")
    List<Booking> findBookingsOfUserBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.end<?2  ORDER BY b.start DESC")
    List<Booking> findBookingsOfUserPast(Long userId, LocalDateTime end);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.start>?2  ORDER BY b.start DESC")
    List<Booking> findBookingsOfUserFuture(Long userId, LocalDateTime start);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.status=?2 ORDER BY b.start DESC")
    List<Booking> findAllBookingsOfUserWithStatus(Long userId, BookingStatus status);

    /**
     * Получение списка всех бронирований для владельца вещи
     */
    @Query("SELECT b FROM Booking b WHERE b.item.id IN ?1 ORDER BY b.start DESC")
    List<Booking> findAllBookingsOfItemsUser(List<Long> itemIds);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN ?1 AND b.start<?2 AND b.end>?3 ORDER BY b.start DESC")
    List<Booking> findBookingsOfUserItemsBetween(List<Long> itemIds, LocalDateTime start, LocalDateTime end);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN ?1 AND b.end<?2  ORDER BY b.start DESC")
    List<Booking> findBookingsOfUserItemsInPast(List<Long> itemIds, LocalDateTime end);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN ?1 AND b.start>?2  ORDER BY b.start DESC")
    List<Booking> findBookingsOfUserItemsInFuture(List<Long> itemIds, LocalDateTime start);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN ?1 AND b.status=?2 ORDER BY b.start DESC")
    List<Booking> findAllBookingsOfUserItemsWithStatus(List<Long> itemIds, BookingStatus status);

    /**
     * получение двух последних бронирований
     */
    @Query("SELECT b FROM Booking b WHERE b.item.owner.id =?1 AND b.item.id = ?2 AND b.end < ?3 AND b.status=?4 ORDER BY b.start DESC")
    List<Booking> findLastBookings(Long userId, Long itemId, LocalDateTime now, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id =?1 AND b.item.id = ?2 AND b.start > ?3 AND b.status=?4 ORDER BY b.start DESC")
    List<Booking> findFutureBookings(Long userId, Long itemId, LocalDateTime now, BookingStatus status);
}

