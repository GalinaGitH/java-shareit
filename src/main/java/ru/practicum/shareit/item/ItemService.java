package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, ItemDto itemDto);

    ItemDto get(long itemId);

    List<ItemDto> getListOfItems(long userId);

    List<ItemDto> searchItemsByText(String text);
}
