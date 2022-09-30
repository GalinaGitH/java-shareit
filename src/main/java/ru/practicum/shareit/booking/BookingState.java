package ru.practicum.shareit.booking;

public enum BookingState {
    //все
    ALL,
    //текущие
    CURRENT,
    //завершенные
    PAST,
    //будущие
    FUTURE,
    //ожидающие подтверждения
    WAITING,
    //отклонённые
    REJECTED;

    static BookingState from(String state) {
        for (BookingState value : BookingState.values()) {
            if (value.name().equals(state)) {
                return value;
            }
        }
        return null;
    }
    }
