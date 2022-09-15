package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item saveItem(long userId, ItemDto itemDto);

    Item updateItem(long userId, ItemDto itemDto);

    Item get(long itemId);

    List<Item> getListOfItems(long userId);

    List<Item> searchItemsByText(String text);
}
