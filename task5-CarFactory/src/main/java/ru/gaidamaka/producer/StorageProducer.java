package ru.gaidamaka.producer;


import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.storage.Storage;

import java.util.Objects;
import java.util.Optional;

public abstract class StorageProducer<T> implements Runnable {
    private T lastProducedThing;
    private final long productionPeriod;
    private final Storage<T> storage;

    public StorageProducer(@NotNull Storage<T> storage, long productionPeriod) {
        this.storage = Objects.requireNonNull(storage, "Storage cant be null");
        this.productionPeriod = productionPeriod;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                lastProducedThing = createThing();
                storage.add(lastProducedThing);
                lastProducedThing = null;
                Thread.sleep(productionPeriod);
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
