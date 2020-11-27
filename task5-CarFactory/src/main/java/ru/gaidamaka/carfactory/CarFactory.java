package ru.gaidamaka.carfactory;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.car.Car;
import ru.gaidamaka.consumer.CarConsumer;
import ru.gaidamaka.producer.CarProducer;
import ru.gaidamaka.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarFactory {
    @NotNull
    private final Storage<Car> storage;

    @NotNull
    private final List<CarProducer> carProducers;

    @NotNull
    private final List<CarConsumer> carConsumers;

    private ExecutorService producersService;

    private ExecutorService consumersService;

    CarFactory(int storageCapacity) {
        this.storage = new Storage<>(storageCapacity);
        this.carProducers = new ArrayList<>();
        this.carConsumers = new ArrayList<>();
    }

    public void addProducer(int producerID, int productionPeriodMS) {
        carProducers.add(new CarProducer(
                storage,
                productionPeriodMS,
                producerID)
        );
    }

    public void addConsumer(int consumerID, int takingPeriodMS) {
        carConsumers.add(new CarConsumer(
                storage,
                takingPeriodMS,
                consumerID)
        );
    }

    public void start() {
        if (isStarted()) {
            throw new IllegalStateException("Factory already started");
        }
        producersService = Executors.newFixedThreadPool(carProducers.size());
        carProducers.forEach(carProducer -> producersService.execute(carProducer));
        consumersService = Executors.newFixedThreadPool(carConsumers.size());
        carConsumers.forEach(carConsumer -> consumersService.execute(carConsumer));
    }

    private boolean isStarted() {
        return producersService != null && consumersService != null;
    }

    public void stop() {
        producersService.shutdownNow();
        consumersService.shutdownNow();
    }
}
