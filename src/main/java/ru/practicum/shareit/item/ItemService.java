package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, ItemDto itemDto);

    ItemDtoWithBookings get(long itemId, long userId);

    List<ItemDtoWithBookings> getListOfItems(long userId);

    List<ItemDto> searchItemsByText(String text);
}
