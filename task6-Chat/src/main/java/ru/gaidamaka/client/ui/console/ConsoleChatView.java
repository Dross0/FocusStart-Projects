package ru.gaidamaka.client.ui.console;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.client.presenter.ClientPresenter;
import ru.gaidamaka.client.ui.ChatView;
import ru.gaidamaka.protocol.message.GeneralMessage;
import ru.gaidamaka.protocol.message.Message;
import ru.gaidamaka.protocol.message.MessageType;
import ru.gaidamaka.protocol.message.ResponseMessage;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class ConsoleChatView implements ChatView {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM HH:mm", Locale.getDefault());

    @NotNull
    private final ConsoleInputChecker consoleInputChecker;

    @NotNull
    private final ConsoleMessageSender messageSender;

    public ConsoleChatView() {
        messageSender = new ConsoleMessageSender(new ConsoleMessageParser());
        consoleInputChecker = new ConsoleInputChecker(messageSender);
        consoleInputChecker.startInputCheck();
    }

    @Override
    public void exit() {
        System.out.println("Chat closing...");
        consoleInputChecker.stopInputCheck();
    }

    @Override
    public void showError(@NotNull String errorMessage) {
        Objects.requireNonNull(errorMessage, "Error message cant be null");
        System.out.println(errorMessage);
    }

    @Override
    public void setClientPresenter(@NotNull ClientPresenter presenter) {
        messageSender.setClientPresenter(presenter);
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
    public void showMessageAboutSuccessConnect() {
        System.out.println("Connection success");
    }

    @Override
    public void showMessageAboutFailedConnect() {
        System.out.println("Connection failed");
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
