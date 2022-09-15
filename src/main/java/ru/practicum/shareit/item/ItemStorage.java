package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item createItem(Item item);

    Item update(Item item);

    void removeItemById(long itemId);

    Item get(long itemId);

    List<Item> findAllItems();
}
