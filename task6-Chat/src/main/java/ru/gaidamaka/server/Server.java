package ru.gaidamaka.server;


import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.protocol.utils.PortValidator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private final Logger logger = LoggerFactory.getLogger(Server.class);

    private final int port;

    @NotNull
    private final RequestHandlersRepository handlersRepository;
    private Thread serverThread;

    public Server(int port) {
        PortValidator.validate(port);
        this.port = port;
        this.handlersRepository = new RequestHandlersRepository();
    }

    public void start() {
        if (serverThread != null) {
            throw new IllegalStateException("Server already started");
        }
        serverThread = new Thread(getRunnable());
        serverThread.start();
    }

    public void stop() {
        if (serverThread == null) {
            throw new IllegalStateException("Server is not started");
        }
        serverThread.interrupt();
        serverThread = null;
    }

    private Runnable getRunnable() {
        return () -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                logger.info("Server starting on port={}", port);
                while (!Thread.currentThread().isInterrupted()) {
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Client connected {}:{}", clientSocket.getInetAddress(), clientSocket.getPort());
                    handlersRepository.startNewClientHandle(clientSocket);
                }
            } catch (IOException e) {
                logger.error("Problem with socket", e);
            }
        };
    }
}
