package ru.gaidamaka.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.protocol.utils.PortValidator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(Server.class);

    private final int port;
    private final RequestHandlersRepository handlersRepository;

    public Server(int port) {
        PortValidator.validate(port);
        this.port = port;
        this.handlersRepository = new RequestHandlersRepository();
    }

    @Override
    public void run() {
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
    }
}
