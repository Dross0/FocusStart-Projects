package ru.gaidamaka.storage;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Storage<T> {
    private static final Logger logger = LoggerFactory.getLogger(Storage.class);

    private final int capacity;
    private final @NotNull List<T> data;

    public Storage(int capacity) {
        this.data = new ArrayList<>(capacity);
        this.capacity = capacity;
    }

    @NotNull
    public T pop() throws InterruptedException {
        synchronized (this) {
            while (data.isEmpty()) {
                logger.info("Consumer wait because storage is empty");
                this.wait();
            }
            logger.info("Consumer stop waiting");
            T detail = data.get(data.size() - 1);
            data.remove(data.size() - 1);
            notifyAll();
            return detail;
        }
    }

    public void add(T thing) throws InterruptedException {
        Objects.requireNonNull(thing);
        synchronized (this) {
            while (data.size() >= capacity) {
                logger.info("Producer wait because storage is full");
                this.wait();
            }
            logger.info("Producer stop waiting");
            data.add(thing);
            notifyAll();
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized int getSize() {
        return data.size();
    }
}
