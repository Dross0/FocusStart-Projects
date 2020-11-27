package ru.gaidamaka.car;


import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.AbstractUniqueObject;

import java.util.Objects;
import java.util.UUID;


public class Car extends AbstractUniqueObject {
    @NotNull
    private final Engine engine;

    @NotNull
    private final Body body;

    public Car(@NotNull Engine engine, @NotNull Body body) {
        this.engine = Objects.requireNonNull(engine, "Engine cant be null");
        this.body = Objects.requireNonNull(body, "Body cant be null");
    }

    @NotNull
    public UUID getEngineUUID() {
        return engine.getUUID();
    }

    @NotNull
    public UUID getBodyUUID() {
        return body.getUUID();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("engine", engine)
                .add("body", body)
                .add("uuid", getUUID())
                .toString();
    }
}
