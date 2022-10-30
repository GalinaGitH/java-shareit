package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    /**
     * добавить новый запрос вещи
     */
    @Override
    public ItemRequestDto createRequest(long userId, NewItemRequestDto newItemRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        LocalDateTime createdDateTime = LocalDateTime.now();
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(newItemRequestDto, createdDateTime, user);
        ItemRequest itemRequestFromStorage = itemRequestRepository.save(itemRequest);
        return itemRequestMapper.toItemRequestDto(itemRequestFromStorage);
    }

    /**
     * получить список своих! запросов вместе с данными об ответах на них.
     */
    @Override
    public List<ItemRequestDto> getRequestsOfUser(long userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        int page = from / size;
        List<ItemRequest> requests = itemRequestRepository.findItemRequestsOfUser(user.getId(), PageRequest.of(page, size));
        return itemRequestMapper.toItemRequestDtoList(requests);
    }

    /**
     * получить список существующих запросов, созданных другими пользователями
     */
    @Override
    public List<ItemRequestDto> getExistingRequestsOfUsers(long userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        int page = from / size;
        List<ItemRequest> requests = itemRequestRepository.findItemRequestsOfAllUsers(user.getId(), PageRequest.of(page, size));
        return itemRequestMapper.toItemRequestDtoList(requests);
    }

    /**
     * получить данные об одном конкретном запросе вместе с данными об ответах на него.
     * посмотреть данные об отдельном запросе может любой пользователь.
     */
    @Override
    public ItemRequestDto getRequest(long userId, long requestId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        ItemRequest itemRequestFromStorage = itemRequestRepository.findById(requestId).orElseThrow(NotFoundException::new);
        return itemRequestMapper.toItemRequestDto(itemRequestFromStorage);
    }
}
