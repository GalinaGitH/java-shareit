package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("SELECT ir FROM ItemRequest ir WHERE ir.requestor.id =?1 ORDER BY ir.created DESC")
    List<ItemRequest> findItemRequestsOfUser(long userId, Pageable pageable);

    @Query("SELECT ir FROM ItemRequest ir WHERE ir.requestor.id <>?1 ORDER BY ir.created DESC")
    List<ItemRequest> findItemRequestsOfAllUsers(long userId, Pageable pageable);
}
