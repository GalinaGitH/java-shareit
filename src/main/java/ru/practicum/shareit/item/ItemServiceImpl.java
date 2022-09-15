package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    /**
     * сохранение вещи
     */
    @Override
    public Item saveItem(long userId, ItemDto itemDto) {
        User owner = userStorage.get(userId);
        if (owner == null) {
            throw new NotFoundException("Owner with id=" + userId + "not found");
        }
        Item item = ItemMapper.toItem(itemDto, owner);
        itemStorage.createItem(item);
        return item;
    }

    /**
     * изменение вещи:Изменить можно название, описание и статус доступа к аренде.
     * Редактировать вещь может только её владелец.
     */
    @Override
    public Item updateItem(long userId, ItemDto itemDto) {
        final Item itemInStorage = itemStorage.get(itemDto.getId());
        if (itemInStorage == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User owner = userStorage.get(userId);
        if (itemInStorage.getOwner().getId().equals(owner.getId())) {
            if (itemDto.getName() != null) {
                itemInStorage.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                itemInStorage.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                itemInStorage.setAvailable(itemDto.getAvailable());
            }
            itemStorage.update(itemInStorage);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return itemInStorage;
    }

    /**
     * Просмотр информации о конкретной вещи по id
     */
    @Override
    public Item get(long itemId) {
        final Item item = itemStorage.get(itemId);
        if (item == null) {
            throw new NotFoundException("Item with id=" + itemId + "not found");
        }
        return item;
    }

    /**
     * получение владельцем списка всех его вещей
     * с указанием названия и описания для каждой
     */
    @Override
    public List<Item> getListOfItems(long userId) {
        User owner = userStorage.get(userId);
        return itemStorage.findAllItems().stream()
                .filter(x -> x.getOwner() == owner)
                .collect(Collectors.toList());
    }

    /**
     * Поиск вещей потенциальным арендатором.
     * система ищет вещи, содержащие текст поиска в названии или описании.
     * поиск возвращает только доступные для аренды вещи.
     */
    @Override
    public List<Item> searchItemsByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemStorage.findAllItems().stream()
                .filter(x -> x.getName().toLowerCase().contains(text.toLowerCase()) || x.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
}
