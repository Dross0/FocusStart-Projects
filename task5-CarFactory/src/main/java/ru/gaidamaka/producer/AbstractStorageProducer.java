package ru.gaidamaka.producer;


import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.storage.Storage;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractStorageProducer<T> implements Runnable {
    private volatile T lastProducedThing;
    private final long productionPeriodMs;

    @NotNull
    private final Storage<T> storage;

    public AbstractStorageProducer(@NotNull Storage<T> storage, long productionPeriodMs) {
        this.storage = Objects.requireNonNull(storage, "Storage cant be null");
        this.productionPeriodMs = productionPeriodMs;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(productionPeriodMs);
                lastProducedThing = createThing();
                storage.add(lastProducedThing);
                lastProducedThing = null;
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    @NotNull
    public Optional<T> getLastProduced() {
        return Optional.ofNullable(lastProducedThing);
    }

    @NotNull
    protected abstract T createThing();
}
