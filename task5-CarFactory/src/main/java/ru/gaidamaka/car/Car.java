package ru.gaidamaka.car;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;


public class Car {
    private final Engine engine;
    private final Body body;
    private final @NotNull UUID uuid;

    public Car(Engine engine, Body body) {
        this.engine = Objects.requireNonNull(engine, "Engine cant be null");
        this.body = Objects.requireNonNull(body, "Body cant be null");
        this.uuid = UUID.randomUUID();

    }

    public @NotNull UUID getEngineId() {
        return engine.getID();
    }

    public @NotNull UUID getBodyId() {
        return body.getID();
    }

    public @NotNull UUID getId() {
        return uuid;
    }

    @Override
    public @NotNull String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("engine", engine)
                .add("body", body)
                .add("id", uuid)
                .toString();
    }
}
