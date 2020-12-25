package ru.gaidamaka.producer;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.car.Body;
import ru.gaidamaka.car.Car;
import ru.gaidamaka.car.Engine;
import ru.gaidamaka.storage.Storage;

public class CarProducer extends AbstractStorageProducer<Car> {
    private static final Logger logger = LoggerFactory.getLogger(CarProducer.class);

    private final int id;

    public CarProducer(@NotNull Storage<Car> storage, long productionPeriod, int id) {
        super(storage, productionPeriod);
        this.id = id;
    }


    @Override
    @NotNull
    protected Car createThing() {
        final Car car = new Car(new Engine(), new Body());
        logger.info("Producer with id={} produce new car={}", id, car);
        return car;
    }
}
