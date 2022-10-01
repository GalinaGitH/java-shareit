package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;

    /**
     * сохранение вещи
     */
    @Transactional
    @Override
    public ItemDto saveItem(long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Item item = ItemMapper.toItem(itemDto, owner);
        Item itemFromStorage = itemRepository.save(item);
        return itemMapper.toItemDto(itemFromStorage);
    }

    /**
     * изменение вещи:Изменить можно название, описание и статус доступа к аренде.
     * Редактировать вещь может только её владелец.
     */
    @Transactional
    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        final Item itemInStorage = itemRepository.findById(itemDto.getId()).orElseThrow(NotFoundException::new);
        User owner = userRepository.findById(userId).orElseThrow();
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
            itemRepository.save(itemInStorage);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return itemMapper.toItemDto(itemInStorage);
    }

    /**
     * Просмотр информации о конкретной вещи по id
     */
    @Override
    public ItemDtoWithBookings get(long itemId, long userId) {
        final Item item = itemRepository.findById(itemId).orElseThrow(NotFoundException::new);
        if (!item.getOwner().getId().equals(userId)) {
            return itemMapper.toItemDtoWithBookings(item, null, null);
        }
        return getItemWithBooking(item, userId, itemId);
    }

    /**
     * получение владельцем списка всех его вещей
     * с указанием названия и описания для каждой
     */
    @Override
    public List<ItemDtoWithBookings> getListOfItems(long userId) {
        User owner = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        List<Item> items = itemRepository.findAll().stream()
                .filter(x -> x.getOwner() == owner)
                .collect(Collectors.toList());
        List<ItemDtoWithBookings> itemsFinal = new ArrayList<>();
        for (Item item : items) {
            ItemDtoWithBookings itemWithBooking = getItemWithBooking(item, userId, item.getId());
            itemsFinal.add(itemWithBooking);
        }
        return itemsFinal;
    }

    /**
     * Поиск вещей потенциальным арендатором.
     * система ищет вещи, содержащие текст поиска в названии или описании.
     * поиск возвращает только доступные для аренды вещи.
     */
    @Override
    public List<ItemDto> searchItemsByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> searchItems = itemRepository.findAll().stream()
                .filter(x -> x.getName().toLowerCase().contains(text.toLowerCase()) || x.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
        return itemMapper.toItemDtoList(searchItems);
    }

    private ItemDtoWithBookings getItemWithBooking(Item item, long ownerId, long itemId) {
        List<Booking> lastBookings = bookingRepository.findLastBookings(ownerId, itemId, LocalDateTime.now(), BookingStatus.APPROVED);
        List<Booking> futureBookings = bookingRepository.findFutureBookings(ownerId, itemId, LocalDateTime.now(), BookingStatus.APPROVED);

        Booking last = lastBookings.stream()
                .max(Comparator.comparing(Booking::getEnd))
                .orElse(null);

        Booking next = futureBookings.stream()
                .findFirst()
                .orElse(null);

        return itemMapper.toItemDtoWithBookings(item, last, next);
    }

}
