package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.user.Create;
import ru.practicum.shareit.user.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class ItemDtoWithBookings {
    @NotNull(groups = {Update.class})
    private long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private Long requestId;
    private List<CommentDto> comments;
    private Booking lastBooking;
    private Booking nextBooking;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = "id")
    public static class Booking {
        Long id;
        Long bookerId;
    }
}

