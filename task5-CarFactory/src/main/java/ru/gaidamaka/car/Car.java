package ru.gaidamaka.car;


import java.util.Objects;


public class Car {
    private static int carNumbers = 0;

    private final Engine engine;
    private final Body body;
    private final int id;

    public Car(Engine engine, Body body) {
        this.engine = Objects.requireNonNull(engine, "Engine cant be null");
        this.body = Objects.requireNonNull(body, "Body cant be null");
        carNumbers++;
        this.id = carNumbers;

    }

    public int getEngineId() {
        return engine.getID();
    }

    public int getBodyId() {
        return body.getID();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("engine", engine)
                .add("body", body)
                .add("id", id)
                .toString();
    }
}
