package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemMapper {
    private final CommentMapper commentMapper;

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId()
        );
    }

    public List<ItemDto> toItemDtoList(List<Item> items) {
        return items.stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                owner,
                itemDto.getAvailable(),
                itemDto.getRequestId()
        );
    }

    public ItemDtoWithBookings toItemDtoWithBookings(Item item, Booking lastBooking, Booking nextBooking) {
        if (lastBooking == null && nextBooking == null) {
            return new ItemDtoWithBookings(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequestId(),
                    commentMapper.toCommentDtoList(item.getComments()),
                    null,
                    null);
        } else if (lastBooking == null) {
            return new ItemDtoWithBookings(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequestId(),
                    commentMapper.toCommentDtoList(item.getComments()),
                    null,
                    new ItemDtoWithBookings.Booking(nextBooking.getId(), nextBooking.getBooker().getId()));
        } else if (nextBooking == null) {
            return new ItemDtoWithBookings(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequestId(),
                    commentMapper.toCommentDtoList(item.getComments()),
                    new ItemDtoWithBookings.Booking(lastBooking.getId(), lastBooking.getBooker().getId()),
                    null);
        } else {
            return new ItemDtoWithBookings(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequestId(),
                    commentMapper.toCommentDtoList(item.getComments()),
                    new ItemDtoWithBookings.Booking(lastBooking.getId(), lastBooking.getBooker().getId()),
                    new ItemDtoWithBookings.Booking(nextBooking.getId(), nextBooking.getBooker().getId())
            );
        }
    }
}
