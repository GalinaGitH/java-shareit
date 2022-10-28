package ru.practicum.gateway.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")

public class CommentRequestDto {

    private Long id;
    @NotBlank(groups = {Create.class})
    private String text;
    private Long itemId;
    private String authorName;
    private LocalDateTime dateOfCreation;

}
