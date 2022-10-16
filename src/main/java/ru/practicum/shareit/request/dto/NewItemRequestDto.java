package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.user.Create;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewItemRequestDto {
    @NotBlank(groups = {Create.class})
    private String description;
}
