package ru.practicum.gateway.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.item.dto.Create;
import ru.practicum.gateway.request.dto.ItemRequestGatewayDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @Validated({Create.class}) @RequestBody ItemRequestGatewayDto itemRequestGatewayDto) {
        log.info("Creating request {}, userId={}",itemRequestGatewayDto, userId);
        return requestClient.createRequest(userId, itemRequestGatewayDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get of requests of user, userId={}", userId);
        return requestClient.getRequestsOfUser(userId, from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getExistingRequestsOfUsers(@RequestHeader("X-Sharer-User-Id") long userId,
                                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                             @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get List of all  existing requests");
        return requestClient.getExistingRequestsOfUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable("requestId") long requestId) {
        log.debug("Request data received, requestId={}", requestId);
        return requestClient.getRequest(userId, requestId);
    }


}
