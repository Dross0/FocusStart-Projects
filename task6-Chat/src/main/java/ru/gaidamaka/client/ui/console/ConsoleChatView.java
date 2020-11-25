package ru.gaidamaka.client.ui.console;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.client.presenter.ClientPresenter;
import ru.gaidamaka.client.ui.ChatView;
import ru.gaidamaka.protocol.message.*;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleChatView implements ChatView {
    public static final String CONNECT_COMMAND = "/CONNECT";
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM HH:mm", Locale.getDefault());
    private ClientPresenter clientPresenter;
    private final Thread inputCheckerThread;

    public ConsoleChatView() {
        inputCheckerThread = new Thread(getInputCheckRunnable());
        startInputCheck();
    }

    @Override
    public void setClientPresenter(@NotNull ClientPresenter clientPresenter) {
        this.clientPresenter = clientPresenter;
    }

    private void startInputCheck() {
        inputCheckerThread.start();
    }

    private void stopInputCheck() {
        inputCheckerThread.interrupt();
    }

    @Override
    public void exit() {
        System.out.println("Chat closing...");
        stopInputCheck();
    }

    @NotNull
    private Runnable getInputCheckRunnable() {
        return () -> {
            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
            while (!Thread.currentThread().isInterrupted()) {
                if (!scanner.hasNextLine()) {
                    continue;
                }
                String rawMessage = scanner.nextLine();
                if (clientPresenter == null) {
                    throw new IllegalStateException("No presenter");
                }
                parseAndSendMessage(rawMessage);
            }
        };
    }

    private void parseAndSendMessage(String rawMessage) {
        try {
            if (isConnectMessage(rawMessage)) {
                parseAndConnect(rawMessage);
                return;
            }
            parseRequestCode(rawMessage).ifPresentOrElse(code -> {
                        if (code == CommandCode.LOGIN) {
                            clientPresenter.onRequestMessageEvent(code, parseUserNameFromLoginMessage(rawMessage));
                        } else {
                            clientPresenter.onRequestMessageEvent(code, null);
                        }
                    },
                    () -> clientPresenter.onGeneralMessageEvent(rawMessage)
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid command: " + e.getMessage());
        }
    }

    private void parseAndConnect(String rawMessage) {
        String[] splitConnectMessage = rawMessage.split(" ", 3);
        try {
            if (splitConnectMessage.length != 3) {
                throw new IllegalArgumentException("Connect message must contain: /CONNECT ip port");
            }
            clientPresenter.onConnectEvent(splitConnectMessage[1].strip(),
                    Integer.parseInt(splitConnectMessage[2].strip())
            );
        } catch (NumberFormatException e) {
            System.out.println("Port must be is integer");
        }
    }

    private boolean isConnectMessage(String rawMessage) {
        return rawMessage.startsWith(CONNECT_COMMAND);
    }

    private String parseUserNameFromLoginMessage(String rawMessage) {
        String[] splitCommandCode = rawMessage.split(" ", 2);
        if (splitCommandCode.length != 2) {
            throw new IllegalArgumentException("Login message must contain: /LOGIN userName");
        }
        return splitCommandCode[1].strip();
    }

    private Optional<CommandCode> parseRequestCode(String rawMessage) {
        for (CommandCode commandCode : CommandCode.values()) {
            if (rawMessage.startsWith("/" + commandCode)) {
                return Optional.of(commandCode);
            }
        }
        return Optional.empty();
    }

    @Override
    public void showError(@NotNull String errorMessage) {
        Objects.requireNonNull(errorMessage, "Error message cant be null");
        System.out.println(errorMessage);
    }

    @Override
    public void showMessage(@NotNull Message message) {
        Objects.requireNonNull(message, "Message cant be null");
        MessageType messageType = message.getType();
        if (messageType == MessageType.SERVER_REQUEST) {
            throw new IllegalStateException("Client cant work with requests");
        } else if (messageType == MessageType.GENERAL_MESSAGE) {
            showGeneralMessage((GeneralMessage) message);
        } else if (messageType == MessageType.SERVER_RESPONSE) {
            showServerResponse((ResponseMessage) message);
        }
    }

    @Override
    public void showConnectionResult(boolean isSuccess) {
        if (isSuccess) {
            System.out.println("Connection success");
        } else {
            System.out.println("Connection failed");
        }
    }

    private void showServerResponse(ResponseMessage message) {
        System.out.println("RESULT=" + message.getContent());
    }

    private void showGeneralMessage(GeneralMessage message) {
        String dateStr = dateFormat.format(message.getSendTime());
        String userStr = message.getSender().getName();
        System.out.println(dateStr + " " + userStr + ": " + message.getContent());
    }
}
