package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
        ItemDto itemDtoSaved = itemService.saveItem(userId, itemDto);
        log.debug("Number of added items: {}", 1);
        return itemDtoSaved;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long itemId,
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
    public List<ItemDtoWithBookings> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(name = "from", defaultValue = "0") int from,
                                                    @RequestParam(name = "size", defaultValue = "10") int size) {
        List<ItemDtoWithBookings> items = itemService.getListOfItems(userId, from, size);
        log.info("Get List of items with userid={}", userId);
        return items;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String text,
                                     @RequestParam(name = "from", defaultValue = "0") int from,
                                     @RequestParam(name = "size", defaultValue = "10") int size) {
        List<ItemDto> items = itemService.searchItemsByText(text, from, size);
        log.info("Get List of items by search={}", text);
        return items;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto saveComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                                  @RequestBody CommentDto commentDto) {
        commentDto.setItemId(itemId);
        CommentDto commentDtoSaved = commentService.saveComment(commentDto, userId);
        log.debug("Added a comment to the item with id =: {}", itemId);
        return commentDtoSaved;
    }
}
