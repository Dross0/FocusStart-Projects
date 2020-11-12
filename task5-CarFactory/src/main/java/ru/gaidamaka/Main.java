package ru.gaidamaka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.carfactory.CarFactory;
import ru.gaidamaka.carfactory.CarFactoryConfigurator;
import ru.gaidamaka.carfactory.exception.CarFactoryConfiguratorException;

import java.io.IOException;
import java.util.Properties;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final String FACTORY_PROPERTIES_PATH = "factory.properties";

    public static void main(String[] args) {
        try {
            final Properties factoryProperties = new Properties();
            factoryProperties.load(Main.class
                    .getClassLoader()
                    .getResourceAsStream(FACTORY_PROPERTIES_PATH)
            );
            final CarFactoryConfigurator factoryConfigurator = new CarFactoryConfigurator(factoryProperties);
            CarFactory carFactory = factoryConfigurator.createFactory();
            carFactory.start();
        } catch (IOException e) {
            logger.error("No such file {" + FACTORY_PROPERTIES_PATH + "}", e);
        } catch (CarFactoryConfiguratorException e) {
            System.out.println(e.getMessage());
            logger.error("Incorrect some properties", e);
        }
    }
}
