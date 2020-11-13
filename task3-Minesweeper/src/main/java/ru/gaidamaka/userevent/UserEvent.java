package ru.gaidamaka.userevent;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class UserEvent {
    private final UserEventType type;

    public UserEvent(@NotNull UserEventType type) {
        this.type = Objects.requireNonNull(type, "User event type cant be null");
    }

    @NotNull
    public UserEventType getType() {
        return type;
    }
}
