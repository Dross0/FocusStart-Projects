package ru.gaidamaka.client;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.client.event.ClientEvent;
import ru.gaidamaka.client.event.ExitEvent;
import ru.gaidamaka.client.event.NewMessageEvent;
import ru.gaidamaka.client.exception.SendMessageException;
import ru.gaidamaka.client.exception.ServerConnectionException;
import ru.gaidamaka.protocol.SerializableObjectsConnection;
import ru.gaidamaka.protocol.message.*;
import ru.gaidamaka.protocol.utils.PortValidator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Client implements ClientObservable {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final List<ClientObserver> clientObservers;
    private Thread clientThread;

    @Nullable
    private User currentUser;
    private SerializableObjectsConnection connection;

    public Client() {
        this.clientObservers = new ArrayList<>();
        this.currentUser = null;
    }

    public void connectToServer(@NotNull InetAddress ip, int port) {
        PortValidator.validate(port);
        try {
            this.connection = new SerializableObjectsConnection(new Socket(ip, port));
        } catch (IOException e) {
            logger.error("Cant connect to server={}:{}", ip, port, e);
            throw new ServerConnectionException("Connection failed", e);
        }
    }

    public void sendGeneralMessage(@NotNull String content) {
        if (!isUserLogged()) {
            throw new IllegalStateException("Cant send message from not logged user");
        }
        sendMessage(new GeneralMessage(content, currentUser));
    }

    public void sendLoginMessage(@NotNull String userName) {
        validateUserName(Objects.requireNonNull(userName, "User name cant be null"));
        sendRequestMessage(CommandCode.LOGIN, new User(userName));
    }

    private void validateUserName(String userName) {
        if (userName.length() > 20) {
            throw new IllegalArgumentException("User name length must be less than 20");
        }
    }

    private void sendRequestMessage(CommandCode code, User user) {
        Objects.requireNonNull(code, "Command code cant be null");
        sendMessage(new RequestMessage(code, user));
        if (code == CommandCode.EXIT) {
            notifyObservers(new ExitEvent());
            close();
        }
    }

    public void sendRequestMessage(@NotNull CommandCode code) {
        if (!isUserLogged()) {
            throw new IllegalStateException("Cant send message from not logged user");
        }
        sendRequestMessage(code, currentUser);
    }

    private void sendMessage(@NotNull Message message) {
        try {
            connection.sendObject(message);
        } catch (IOException e) {
            logger.error("Cant send message={}", message, e);
            throw new SendMessageException("Cant send message=" + message, e);
        }
    }


    public boolean isUserLogged() {
        return currentUser != null;
    }


    public void close() {
        try {
            clientThread.interrupt();
            connection.close();
        } catch (IOException e) {
            logger.error("Error while connection closing", e);
        }
    }

    public void start() {
        if (clientThread != null) {
            throw new IllegalStateException("Client already started");
        }
        clientThread = new Thread(getReceiveMessagesRunnable());
        clientThread.start();
    }

    public void stop() {
        if (clientThread == null) {
            throw new IllegalStateException("Cant stop not running client");
        }
        close();
    }

    private Runnable getReceiveMessagesRunnable() {
        return () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Message message = (Message) connection.readObject();
                    checkLoginStatus(message);
                    notifyObservers(new NewMessageEvent(message));
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Cant receive message, client={} exit", currentUser, e);
            } finally {
                close();
            }
        };
    }

    private void checkLoginStatus(Message message) {
        if (currentUser != null) {
            return;
        }
        if (message.getType() == MessageType.SERVER_RESPONSE) {
            ResponseMessage response = (ResponseMessage) message;
            if (response.getRequestMessage().getCommandCode() == CommandCode.LOGIN
                    && response.getResponseStatusCode() == ResponseStatusCode.SUCCESS) {
                currentUser = response.getRequestMessage().getUser();
            }
        }
    }

    @Override
    public void notifyObservers(@NotNull ClientEvent event) {
        for (ClientObserver clientObserver : clientObservers) {
            clientObserver.update(event);
        }
    }

    @Override
    public void addObserver(@NotNull ClientObserver clientObserver) {
        clientObservers.add(Objects.requireNonNull(clientObserver));
    }

    @Override
    public void removeObserver(@NotNull ClientObserver clientObserver) {
        clientObservers.remove(Objects.requireNonNull(clientObserver));
    }
}
