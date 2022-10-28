package ru.practicum.gateway.item;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.item.dto.CommentRequestDto;
import ru.practicum.gateway.item.dto.Create;
import ru.practicum.gateway.item.dto.ItemRequestDto;
import ru.practicum.gateway.item.dto.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Validated({Create.class}) @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Creating item, userId={}", userId);
        return itemClient.createItem(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Validated({Update.class}) @PathVariable("itemId") long itemId,
                                             @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Item updated");
        return itemClient.updateItem(userId, itemId, itemRequestDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Get item by id={}", itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get List of items with userid={}", userId);
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam("text") String text,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get List of items by search={}", text);
        return itemClient.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                                              @Validated({Create.class}) @RequestBody CommentRequestDto commentRequestDto) {
        log.info("Added a comment to the item with id =: {}", itemId);
        return itemClient.addComment(userId, itemId, commentRequestDto);
    }
}
