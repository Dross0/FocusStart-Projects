package ru.gaidamaka;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class AbstractUniqueObject {
    @NotNull
    private final UUID uuid = UUID.randomUUID();

    @NotNull
    public UUID getUUID() {
        return uuid;
    }
}
