package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.user.Create;
import ru.practicum.shareit.user.Update;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        ItemDto itemDtoSaved = itemService.saveItem(userId, itemDto);
        log.debug("Number of added items: {}", 1);
        return itemDtoSaved;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Update.class}) @PathVariable("itemId") long itemId,
                              @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        ItemDto itemDtoUpdated = itemService.updateItem(userId, itemDto);
        log.debug("Item updated");
        return itemDtoUpdated;
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookings getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Get item by id={}", itemId);
        ItemDtoWithBookings itemDto = itemService.get(itemId, userId);
        return itemDto;
    }

    @GetMapping
    public List<ItemDtoWithBookings> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<ItemDtoWithBookings> items = itemService.getListOfItems(userId);
        log.info("Get List of items with userid={}", userId);
        return items;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String text) {
        List<ItemDto> items = itemService.searchItemsByText(text);
        log.info("Get List of items by search={}", text);
        return items;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto saveComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @Validated({Create.class}) @RequestBody CommentDto commentDto) {
        commentDto.setItemId(itemId);
        CommentDto commentDtoSaved = commentService.saveComment(commentDto, userId);
        log.debug("Added a comment to the item with id =: {}", itemId);
        return commentDtoSaved;
    }
}
