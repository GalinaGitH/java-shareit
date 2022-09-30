package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;


public interface CommentService {

    CommentDto saveComment(CommentDto commentDto, long userId);
}
