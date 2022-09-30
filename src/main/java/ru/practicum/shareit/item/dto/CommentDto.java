package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.user.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class CommentDto {

    private Long id;
    @NotBlank(groups = {Create.class})
    private String text;
    private Long itemId;
    private String authorName;
    private LocalDateTime dateOfCreation;

}
