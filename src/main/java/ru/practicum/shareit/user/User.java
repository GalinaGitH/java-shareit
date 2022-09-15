package ru.practicum.shareit.user;

import lombok.*;


/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class User {

    private Long id;
    private String name;
    private String email;
}
