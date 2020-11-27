package ru.gaidamaka.consumer;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.storage.Storage;

import java.util.Objects;

public abstract class AbstractStorageConsumer<T> implements Runnable {
    private final long takingPeriodMs;

    @NotNull
    private final Storage<T> storage;

    public AbstractStorageConsumer(@NotNull Storage<T> storage, long takingPeriodMs) {
        this.storage = Objects.requireNonNull(storage, "Storage cant be null");
        this.takingPeriodMs = takingPeriodMs;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(takingPeriodMs);
                useThing(storage.pop());
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    protected abstract void useThing(@NotNull T pop);
}
