package ru.gaidamaka.car;


import java.util.Objects;
import java.util.UUID;


public class Car {
    private final Engine engine;
    private final Body body;
    private final UUID uuid;

    public Car(Engine engine, Body body) {
        this.engine = Objects.requireNonNull(engine, "Engine cant be null");
        this.body = Objects.requireNonNull(body, "Body cant be null");
        this.uuid = UUID.randomUUID();

    }

    public UUID getEngineId() {
        return engine.getID();
    }

    public UUID getBodyId() {
        return body.getID();
    }

    public UUID getId() {
        return uuid;
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("engine", engine)
                .add("body", body)
                .add("id", uuid)
                .toString();
    }
}
