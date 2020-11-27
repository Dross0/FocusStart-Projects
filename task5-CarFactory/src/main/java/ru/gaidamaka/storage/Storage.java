package ru.gaidamaka.storage;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Storage<T> {
    private final int capacity;

    @NotNull
    private final BlockingQueue<T> data;

    public Storage(int capacity) {
        this.data = new ArrayBlockingQueue<>(capacity);
        this.capacity = capacity;
    }

    @NotNull
    public T pop() throws InterruptedException {
        return data.take();
    }

    public void add(@NotNull T thing) {
        Objects.requireNonNull(thing);
        data.add(thing);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSize() {
        return data.size();
    }
}
