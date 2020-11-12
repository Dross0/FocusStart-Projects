package ru.gaidamaka.consumer;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.storage.Storage;

import java.util.Objects;

public abstract class StorageConsumer<T> implements Runnable {
    private final long takingPeriod;
    private final Storage<T> storage;

    public StorageConsumer(@NotNull Storage<T> storage, long takingPeriod) {
        this.storage = Objects.requireNonNull(storage, "Storage cant be null");
        this.takingPeriod = takingPeriod;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                useThing(storage.pop());
                Thread.sleep(takingPeriod);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    protected abstract void useThing(@NotNull T pop);
}
