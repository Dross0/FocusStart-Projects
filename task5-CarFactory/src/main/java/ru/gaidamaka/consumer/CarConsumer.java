package ru.gaidamaka.consumer;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.car.Car;
import ru.gaidamaka.storage.Storage;

import java.util.Objects;

public class CarConsumer extends AbstractStorageConsumer<Car> {
    private static final Logger logger = LoggerFactory.getLogger(CarConsumer.class);

    private final int id;

    public CarConsumer(@NotNull Storage<Car> storage, long takingPeriod, int id) {
        super(storage, takingPeriod);
        this.id = id;
    }

    @Override
    protected void useThing(@NotNull Car car) {
        Objects.requireNonNull(car, "Car cant be null");
        logger.info("Consumer with id={} take car={}", id, car);
    }
}
