package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.NotFoundException;

import java.util.Objects;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingService bookingService;
    private final CommentMapper commentMapper;

    @Transactional
    @Override
    public CommentDto saveComment(CommentDto commentDto, long userId) {
        final Item itemInStorage = itemRepository.findById(commentDto.getItemId())
                .orElseThrow(NotFoundException::new);
        final User author = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        if (!bookingService.getBookingOfUser(userId, BookingState.PAST, 0, 10).stream()
                .anyMatch(b -> Objects.equals(b.getItem().getId(), itemInStorage.getId()))) {
            throw new IllegalArgumentException("You can leave a comment only after booking a thing");
        }

        Comment comment = commentMapper.toComment(commentDto, author, itemInStorage);
        itemInStorage.addComment(comment);
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }
}
