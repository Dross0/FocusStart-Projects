package ru.gaidamaka.client.presenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.client.Client;
import ru.gaidamaka.client.ClientObserver;
import ru.gaidamaka.client.event.ClientEvent;
import ru.gaidamaka.client.event.EventType;
import ru.gaidamaka.client.event.NewMessageEvent;
import ru.gaidamaka.client.exception.SendMessageException;
import ru.gaidamaka.client.exception.ServerConnectionException;
import ru.gaidamaka.client.ui.ChatView;
import ru.gaidamaka.protocol.message.CommandCode;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class ConsolePresenter implements ClientPresenter, ClientObserver {
    private static final Logger logger = LoggerFactory.getLogger(ConsolePresenter.class);
    private final Client client;
    private final ChatView consoleView;

    public ConsolePresenter(@NotNull Client client, @NotNull ChatView consoleView) {
        this.client = Objects.requireNonNull(client, "Client cant be null");
        this.consoleView = Objects.requireNonNull(consoleView, "View cant be null");
    }

    @Override
    public void start() {
        client.addObserver(this);
        consoleView.setClientPresenter(this);
    }

    @Override
    public void onGeneralMessageEvent(@NotNull String content) {
        try {
            client.sendGeneralMessage(content);
        } catch (IllegalStateException e) {
            logger.warn("Client cant send message until login", e);
            consoleView.showError(e.getMessage());
        } catch (SendMessageException e) {
            logger.error("Cant send message", e);
            consoleView.showError(e.getMessage());
        }
    }

    @Override
    public void onRequestMessageEvent(@NotNull CommandCode code, @Nullable String userName) {
        Objects.requireNonNull(code);
        try {
            if (code == CommandCode.LOGIN && userName != null) {
                client.sendLoginMessage(userName);
            } else {
                client.sendRequestMessage(code);
            }
        } catch (SendMessageException e) {
            logger.warn("Error while send request", e);
            consoleView.showError(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("User name={} is not valid", userName, e);
            consoleView.showError(e.getMessage());
        } catch (IllegalStateException e) {
            logger.warn("User not logged", e);
            consoleView.showError(e.getMessage());
        }
    }

    @Override
    public void onConnectEvent(@NotNull String host, int port) {
        try {
            InetAddress address = InetAddress.getByName(host);
            client.connectToServer(address, port);
            client.start();
            consoleView.showConnectionResult(true);
        } catch (UnknownHostException e) {
            logger.error("Invalid ip={}", host, e);
            consoleView.showError("Invalid ip=" + host);
        } catch (ServerConnectionException e) {
            logger.error("Connect to server={}:{} failed", host, port, e);
            consoleView.showConnectionResult(false);
        } catch (IllegalStateException e) {
            logger.warn("Client already connected", e);
            consoleView.showConnectionResult(false);
        }
    }


    @Override
    public void update(@NotNull ClientEvent event) {
        if (event.getType() == EventType.EXIT) {
            consoleView.exit();
        } else if (event.getType() == EventType.NEW_MESSAGE) {
            NewMessageEvent newMessageEvent = (NewMessageEvent) event;
            consoleView.showMessage(newMessageEvent.getMessage());
        } else {
            throw new IllegalStateException("Unknown event type");
        }
    }
}
