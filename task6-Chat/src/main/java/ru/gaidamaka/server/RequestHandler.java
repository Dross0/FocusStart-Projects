package ru.gaidamaka.server;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.protocol.message.*;
import ru.gaidamaka.server.exception.InvalidUserNameException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.StringJoiner;


public class RequestHandler implements Runnable, ClientHandler {
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket socket;
    private final ClientsRepository clientsRepository;
    private ObjectOutputStream writer;
    private User currentUser;


    public RequestHandler(@NotNull Socket socket, @NotNull ClientsRepository clientsRepository) {
        this.socket = Objects.requireNonNull(socket, "Socket cant be null");
        this.clientsRepository = Objects.requireNonNull(clientsRepository, "Clients repository cant be null");
    }

    private boolean isUserLogged() {
        return currentUser != null;
    }

    @Override
    public void run() {
        try (socket;
             ObjectInputStream reader = new ObjectInputStream(socket.getInputStream())) {

            this.writer = new ObjectOutputStream(socket.getOutputStream());
            while (!Thread.currentThread().isInterrupted()) {
                Message message = (Message) reader.readObject();
                handleMessage(message);
                logger.info("Get message {" + message.getContent() + "}" + " Type=" + message.getType());
            }
        } catch (IOException e) {
            logger.error("Error while reading", e);
        } catch (ClassNotFoundException e) {
            logger.error("Cant serialize message", e);
        }
    }

    private void handleMessage(Message message) {
        switch (message.getType()) {
            case SERVER_REQUEST:
                handleRequest((RequestMessage) message);
                break;
            case GENERAL_MESSAGE:
                handleGeneralMessage((GeneralMessage) message);
                break;
            default:
                throw new IllegalStateException("Cant recognize message type or get server response message");
        }
    }

    private void handleGeneralMessage(GeneralMessage message) {
        clientsRepository
                .getAllClientHandlers()
                .forEach(clientHandler -> clientHandler.sendMessage(message));
    }

    private void handleRequest(RequestMessage message) {
        final CommandCode commandCode = message.getCommandCode();
        if (!isUserLogged() && commandCode != CommandCode.LOGIN) {
            throw new IllegalStateException("Cant receive message before login");
        }
        ResponseStatusCode statusCode = ResponseStatusCode.SUCCESS;
        String usersList = null;
        if (commandCode == CommandCode.LOGIN) {
            statusCode = loginUser(message);
        } else if (commandCode == CommandCode.EXIT) {
            clientsRepository.removeUser(currentUser);
        } else if (commandCode == CommandCode.USER_LIST) {
            usersList = generateUserListString();
        }
        if (commandCode.isNeedResponse()) {
            sendResponse(usersList, message, statusCode);
        }
    }

    private void sendResponse(String content, RequestMessage requestMessage, ResponseStatusCode statusCode) {
        ResponseMessage message = content == null
                ? new ResponseMessage(requestMessage, statusCode)
                : new ResponseMessage(content, requestMessage, statusCode);
        sendMessage(message);
    }

    private String generateUserListString() {
        StringJoiner joiner = new StringJoiner(",\n");
        clientsRepository.getAllUsers()
                .forEach(user -> joiner.add(user.toString()));
        return joiner.toString();
    }

    private ResponseStatusCode loginUser(RequestMessage message) {
        try {
            clientsRepository.registerUser(message.getUser(), this);
            currentUser = message.getUser();
            return ResponseStatusCode.SUCCESS;
        } catch (InvalidUserNameException e) {
            logger.warn("User = {} already exist", message.getUser());
            return ResponseStatusCode.FAILURE;
        }
    }

    @Override
    public void sendMessage(@NotNull Message message) {
        try {
            writer.writeObject(message);
            writer.flush();
        } catch (IOException e) {
            logger.error("Cant send message={}", message, e);
        }
    }
}