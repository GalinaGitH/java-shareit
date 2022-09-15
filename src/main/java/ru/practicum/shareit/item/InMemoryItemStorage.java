package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items = new LinkedHashMap<>();
    private long nextId = 0;

    private long getNextId() {
        return ++nextId;
    }

    @Override
    public Item createItem(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void removeItemById(long itemId) {
        items.remove(itemId);
    }

    @Override
    public Item get(long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> findAllItems() {
        List<Item> values = new ArrayList<>(items.values());
        return values;
    }
}
