package ru.gaidamaka.server;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.protocol.SerializableObjectsConnection;
import ru.gaidamaka.protocol.message.*;
import ru.gaidamaka.server.exception.InvalidUserNameException;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.StringJoiner;


public class RequestHandler implements Runnable, ClientHandler {
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Object writeToClientLock = new Object();

    @NotNull
    private final Socket socket;
    @NotNull
    private final ClientsRepository clientsRepository;

    private User currentUser;
    private SerializableObjectsConnection connection;

    public RequestHandler(@NotNull Socket socket, @NotNull ClientsRepository clientsRepository) {
        this.socket = Objects.requireNonNull(socket, "Socket cant be null");
        this.clientsRepository = Objects.requireNonNull(clientsRepository, "Clients repository cant be null");
    }

    private boolean isUserLogged() {
        return currentUser != null;
    }

    @Override
    public void run() {
        try {
            connection = new SerializableObjectsConnection(socket);
            while (!Thread.currentThread().isInterrupted()) {
                Message message = (Message) connection.readObject();
                handleMessage(message);
                logger.info("Get message {} Type={}", message.getContent(), message.getType());
            }
        } catch (IOException e) {
            logger.error("Error while reading", e);
            closeUserSession();
        } catch (ClassNotFoundException e) {
            logger.error("Cant serialize message", e);
            closeUserSession();
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
            closeUserSession();
        } else if (commandCode == CommandCode.USER_LIST) {
            usersList = generateUserListString();
        }
        if (commandCode.isNeedResponse()) {
            sendResponse(usersList, message, statusCode);
        }
    }

    private void removeFromRepository() {
        if (currentUser == null) {
            clientsRepository.removeUnregisteredConnection(this);
        } else {
            clientsRepository.removeUser(currentUser);
        }
    }

    private void closeUserSession() {
        removeFromRepository();
        if (isUserLogged()) {
            notifyAllAboutUserDisconnect(currentUser);
        }
        Thread.currentThread().interrupt();
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
            notifyAllAboutNewUser(currentUser);
            return ResponseStatusCode.SUCCESS;
        } catch (InvalidUserNameException e) {
            logger.warn("User = {} already exist", message.getUser(), e);
        } catch (IllegalArgumentException e) {
            logger.warn("Registration problem", e);
        }
        return ResponseStatusCode.FAILURE;
    }

    private void notifyAllAboutNewUser(User user) {
        String content = "User " + user.getName() + " was connected";
        handleGeneralMessage(new GeneralMessage(content, user));
    }

    private void notifyAllAboutUserDisconnect(User user) {
        String content = "User " + user.getName() + " was disconnected";
        handleGeneralMessage(new GeneralMessage(content, user));
    }

    @Override
    public void sendMessage(@NotNull Message message) {
        try {
            synchronized (writeToClientLock) {
                connection.sendObject(message);
            }
        } catch (IOException e) {
            logger.error("Cant send message={}", message, e);
        }
    }
}