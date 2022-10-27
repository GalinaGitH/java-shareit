package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class CommentDto {

    private Long id;
    private String text;
    private Long itemId;
    private String authorName;
    private LocalDateTime dateOfCreation;

}
