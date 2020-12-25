package ru.gaidamaka.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class ServerMain {
    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);
    public static final String SERVER_CONFIG_PATH = "serverConfig.properties";
    public static final int DEFAULT_SERVER_PORT = 2048;

    public static void main(String[] args) {
        Server server = new Server(
                readServerPort().orElse(DEFAULT_SERVER_PORT)
        );
        server.start();
    }

    private static Optional<Integer> readServerPort() {
        try (InputStream propertiesStream = ServerMain.class
                .getClassLoader()
                .getResourceAsStream(SERVER_CONFIG_PATH)) {
            if (propertiesStream == null) {
                logger.error("Cant open server config, path={}", SERVER_CONFIG_PATH);
                return Optional.empty();
            }
            Properties properties = new Properties();
            properties.load(propertiesStream);
            String portStr = properties.getProperty("server.port");
            return Optional.of(Integer.parseInt(portStr, 10));
        } catch (IOException e) {
            logger.error("Cant open server config, path={}", SERVER_CONFIG_PATH, e);
        } catch (NumberFormatException e) {
            logger.error("Cant get server.port from config={}", SERVER_CONFIG_PATH, e);
        }
        return Optional.empty();
    }
}
