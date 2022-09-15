package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class ItemRequest {

    @NotNull
    private long id;
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;//текст запроса, содержащий описание требуемой вещи;
    private User requestor;//пользователь, создавший запрос;
    private LocalDateTime created;//дата и время создания запроса.
}
