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
import ru.gaidamaka.protocol.message.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class ChatClientPresenter implements ClientPresenter, ClientObserver {
    private static final Logger logger = LoggerFactory.getLogger(ChatClientPresenter.class);

    @NotNull
    private final Client client;
    @NotNull
    private final ChatView view;

    public ChatClientPresenter(@NotNull Client client, @NotNull ChatView view) {
        this.client = Objects.requireNonNull(client, "Client cant be null");
        this.view = Objects.requireNonNull(view, "View cant be null");
    }

    @Override
    public void start() {
        client.addObserver(this);
        view.setClientPresenter(this);
    }

    @Override
    public void onRequestMessageEvent(@NotNull CommandCode code) {
        onRequestMessageEvent(code, null);
    }

    @Override
    public void onGeneralMessageEvent(@NotNull String content) {
        try {
            client.sendGeneralMessage(content);
        } catch (IllegalStateException e) {
            logger.warn("Client cant send message until login", e);
            view.showError(e.getMessage());
        } catch (SendMessageException e) {
            logger.error("Cant send message", e);
            view.showError(e.getMessage());
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
            view.showError(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("User name={} is not valid", userName, e);
            view.showError(e.getMessage());
        } catch (IllegalStateException e) {
            logger.warn("User not logged", e);
            view.showError(e.getMessage());
        }
    }

    @Override
    public void onConnectEvent(@NotNull String host, int port) {
        try {
            InetAddress address = InetAddress.getByName(host);
            client.connectToServer(address, port);
            client.start();
            view.showMessageAboutSuccessConnect();
        } catch (UnknownHostException e) {
            logger.error("Invalid ip={}", host, e);
            view.showMessageAboutFailedConnect("Invalid ip=" + host);
        } catch (ServerConnectionException e) {
            logger.error("Connect to server={}:{} failed", host, port, e);
            view.showMessageAboutFailedConnect(String.format("Connect to server=%s:%d failed", host, port));
        } catch (IllegalStateException e) {
            logger.warn("Client already connected", e);
            view.showMessageAboutFailedConnect("Client already connected");
        }
    }


    @Override
    public void update(@NotNull ClientEvent event) {
        if (event.getType() == EventType.EXIT) {
            view.exit();
        } else if (event.getType() == EventType.NEW_MESSAGE) {
            NewMessageEvent newMessageEvent = (NewMessageEvent) event;
            handleReceivingNewMessage(newMessageEvent.getMessage());
        } else {
            throw new IllegalStateException("Unknown event type");
        }
    }

    private void handleReceivingNewMessage(Message message) {
        Objects.requireNonNull(message, "Message cant be null");
        MessageType messageType = message.getType();
        if (messageType == MessageType.SERVER_REQUEST) {
            throw new IllegalStateException("Client cant work with requests");
        } else if (messageType == MessageType.GENERAL_MESSAGE) {
            view.showGeneralMessage((GeneralMessage) message);
        } else if (messageType == MessageType.SERVER_RESPONSE) {
            handleReceivingResponse((ResponseMessage) message);
        }
    }

    private void handleReceivingResponse(ResponseMessage message) {
        if (message.getResponseStatusCode() == ResponseStatusCode.SUCCESS) {
            handleSuccessResponse(message);
        } else if (message.getResponseStatusCode() == ResponseStatusCode.FAILURE) {
            handleFailureResponse(message);
        }

    }

    private void handleFailureResponse(ResponseMessage message) {
        RequestMessage requestMessage = message.getRequestMessage();
        if (requestMessage.getCommandCode() == CommandCode.LOGIN) {
            view.showLoginFail(requestMessage.getUser().getName());
        }
    }

    private void handleSuccessResponse(ResponseMessage message) {
        RequestMessage requestMessage = message.getRequestMessage();
        if (requestMessage.getCommandCode() == CommandCode.USER_LIST) {
            view.showUserList(message.getContent());
        }
    }
}
