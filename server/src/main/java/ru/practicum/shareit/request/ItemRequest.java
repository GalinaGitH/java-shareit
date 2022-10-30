package ru.practicum.shareit.request;

import lombok.*;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "item_requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;//текст запроса, содержащий описание требуемой вещи;

    @JoinColumn(name = "requestor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User requestor;//пользователь, создавший запрос;

    @Column(name = "created_at")
    private LocalDateTime created;//дата и время создания запроса.

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requestId")
    private final List<Item> items = new ArrayList<>();

}
