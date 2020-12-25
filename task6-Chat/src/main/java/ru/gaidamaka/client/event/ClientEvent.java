package ru.gaidamaka.client.event;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class ClientEvent {
    @NotNull
    private final EventType type;

    public ClientEvent(@NotNull EventType type) {
        this.type = Objects.requireNonNull(type, "Type cant be null");
    }

    @NotNull
    public EventType getType() {
        return type;
    }
}
