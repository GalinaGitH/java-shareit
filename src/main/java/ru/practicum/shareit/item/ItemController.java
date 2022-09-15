package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.Create;
import ru.practicum.shareit.user.Update;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        Item item = itemService.saveItem(userId, itemDto);
        log.debug("Number of added items: {}", 1);
        return itemMapper.toItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Update.class}) @PathVariable("itemId") long itemId,
                              @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        Item itemUpdated = itemService.updateItem(userId, itemDto);
        log.debug("Item updated");
        return itemMapper.toItemDto(itemUpdated);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId) {
        log.info("Get item by id={}", itemId);
        Item item = itemService.get(itemId);
        return itemMapper.toItemDto(item);
    }

    @GetMapping
    public List<ItemDto> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<Item> items = itemService.getListOfItems(userId);
        log.info("Get List of items with userid={}", userId);
        return itemMapper.toItemDtoList(items);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String text) {
        List<Item> items = itemService.searchItemsByText(text);
        log.info("Get List of items by search={}", text);
        return itemMapper.toItemDtoList(items);
    }
}
