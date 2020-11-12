package ru.gaidamaka.carfactory;

import ru.gaidamaka.car.Car;
import ru.gaidamaka.consumer.CarConsumer;
import ru.gaidamaka.producer.CarProducer;
import ru.gaidamaka.storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class CarFactory {
    private final Storage<Car> storage;
    private final List<Thread> carProducers;
    private final List<Thread> carConsumers;

    public CarFactory(int storageCapacity) {
        this.storage = new Storage<>(storageCapacity);
        this.carProducers = new ArrayList<>();
        this.carConsumers = new ArrayList<>();
    }

    public void addProducer(int producerID, int productionPeriodMS) {
        Thread producerThread = new Thread(new CarProducer(storage, productionPeriodMS, producerID));
        producerThread.setName("CarProducer-" + producerID);
        carProducers.add(producerThread);
    }

    public void addConsumer(int consumerID, int takingPeriodMS) {
        Thread consumerThread = new Thread(new CarConsumer(storage, takingPeriodMS, consumerID));
        consumerThread.setName("CarConsumer-" + consumerID);
        carConsumers.add(consumerThread);
    }

    public void start() {
        carProducers.forEach(Thread::start);
        carConsumers.forEach(Thread::start);
    }

    public void stop() {
        carConsumers.forEach(Thread::interrupt);
        carProducers.forEach(Thread::interrupt);
    }
}
