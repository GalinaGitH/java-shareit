package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.user.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Create.class}) @RequestBody NewItemRequestDto newItemRequestDto) {
        ItemRequestDto itemRequestDtoCreated = itemRequestService.createRequest(userId, newItemRequestDto);
        log.debug("ItemRequest created");
        return itemRequestDtoCreated;
    }

    @GetMapping
    public List<ItemRequestDto> getRequestsOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        List<ItemRequestDto> bookings = itemRequestService.getRequestsOfUser(userId, from, size);
        log.info("Get List of requests of user");
        return bookings;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getExistingRequestsOfUsers(@RequestHeader("X-Sharer-User-Id") long userId,
                                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                           @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        List<ItemRequestDto> bookings = itemRequestService.getExistingRequestsOfUsers(userId, from, size);
        log.info("Get List of all  existing requests");
        return bookings;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable("requestId") long requestId) {
        ItemRequestDto itemRequestDto = itemRequestService.getRequest(userId, requestId);
        log.debug("Request data received");
        return itemRequestDto;
    }


}
