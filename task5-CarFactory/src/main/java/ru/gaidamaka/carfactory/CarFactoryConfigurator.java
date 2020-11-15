package ru.gaidamaka.carfactory;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.carfactory.exception.CarFactoryConfiguratorException;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.Properties;

public class CarFactoryConfigurator {
    private static final int DEFAULT_PRODUCTION_PERIOD_MS = 1000;
    private static final int DEFAULT_TAKING_PERIOD_MS = 500;
    private static final int MIN_CONSUMERS_NUMBER = 1;
    private static final int MAX_CONSUMERS_NUMBER = 100;
    private static final int MIN_PRODUCERS_NUMBER = 1;
    private static final int MAX_PRODUCERS_NUMBER = 100;
    private static final int MIN_STORAGE_CAPACITY = 1;
    private static final int MIN_PRODUCTION_PERIOD = 1;
    private static final int MIN_TAKING_PERIOD = 1;

    private final int consumersNumber;
    private final int producersNumber;
    private final int productionPeriodMS;
    private final int takingPeriodMS;
    private final int storageCapacity;

    public CarFactoryConfigurator(@NotNull Properties properties) {
        Objects.requireNonNull(properties, "Properties cant be null");
        this.consumersNumber = getIntegerProperty(properties, "consumers.number")
                .orElseThrow(() -> new CarFactoryConfiguratorException("Consumers number is unavailable"));
        validateParameter("Consumers number", consumersNumber, MIN_CONSUMERS_NUMBER, MAX_CONSUMERS_NUMBER);
        this.producersNumber = getIntegerProperty(properties, "producers.number")
                .orElseThrow(() -> new CarFactoryConfiguratorException("Producers number is unavailable"));
        validateParameter("Producers number", producersNumber, MIN_PRODUCERS_NUMBER, MAX_PRODUCERS_NUMBER);
        this.storageCapacity = getIntegerProperty(properties, "storage.capacity")
                .orElseThrow(() -> new CarFactoryConfiguratorException("Storage capacity is unavailable"));
        validateParameter("Storage capacity", storageCapacity, MIN_STORAGE_CAPACITY);
        this.takingPeriodMS = getIntegerProperty(properties, "taking.period")
                .orElse(DEFAULT_TAKING_PERIOD_MS);
        validateParameter("Taking period", takingPeriodMS, MIN_TAKING_PERIOD);
        this.productionPeriodMS = getIntegerProperty(properties, "production.period")
                .orElse(DEFAULT_PRODUCTION_PERIOD_MS);
        validateParameter("Production period", productionPeriodMS, MIN_PRODUCTION_PERIOD);
    }

    public @NotNull CarFactory createFactory() {
        CarFactory carFactory = new CarFactory(storageCapacity);
        for (int producerID = 0; producerID < producersNumber; producerID++) {
            carFactory.addProducer(producerID, productionPeriodMS);
        }
        for (int consumerID = 0; consumerID < consumersNumber; consumerID++) {
            carFactory.addConsumer(consumerID, takingPeriodMS);
        }
        return carFactory;
    }

    private OptionalInt getIntegerProperty(@NotNull Properties properties, String key) {
        try {
            return OptionalInt.of(Integer.parseInt(properties.getProperty(key)));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }

    private void validateParameter(String parameterName, int value, int minValue) {
        this.validateParameter(parameterName, value, minValue, Integer.MAX_VALUE);
    }

    private void validateParameter(String parameterName, int value, int minValue, int maxValue) {
        if (value < minValue || value > maxValue)
            throw new CarFactoryConfiguratorException(parameterName + "=" + value + " not from valid interval:["
                    + minValue + ", " + maxValue + "]");
    }
}
