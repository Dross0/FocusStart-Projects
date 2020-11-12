package ru.gaidamaka.carfactory.exception;

public class CarFactoryConfiguratorException extends RuntimeException {
    public CarFactoryConfiguratorException(String errorMessage) {
        super(errorMessage);
    }

    public CarFactoryConfiguratorException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public CarFactoryConfiguratorException(Throwable cause) {
        super(cause);
    }
}