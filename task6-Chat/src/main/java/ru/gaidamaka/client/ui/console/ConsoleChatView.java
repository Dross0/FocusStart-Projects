package ru.gaidamaka.client.ui.console;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.client.presenter.ClientPresenter;
import ru.gaidamaka.client.ui.ChatView;
import ru.gaidamaka.protocol.message.GeneralMessage;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class ConsoleChatView implements ChatView {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM HH:mm", Locale.getDefault());
    private static final String LOGIN_FAIL_OUTPUT_FORMAT = "Имя - %s уже занято";
    private static final String USER_LIST_OUTPUT_FORMAT = "Список пользователей: %s";
    private static final String GENERAL_MESSAGE_OUTPUT_FORMAT = "%s %s: %s";

    @NotNull
    private final ConsoleInputChecker consoleInputChecker;

    @NotNull
    private final ConsoleMessageSender messageSender;

    public ConsoleChatView() {
        messageSender = new ConsoleMessageSender(new ConsoleMessageParser());
        consoleInputChecker = new ConsoleInputChecker(messageSender);
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
    public void showLoginFail(@NotNull String userName) {
        System.out.println(String.format(LOGIN_FAIL_OUTPUT_FORMAT, userName));
    }

    @Override
    public void showUserList(@NotNull String userList) {
        System.out.println(String.format(USER_LIST_OUTPUT_FORMAT, userList));
    }

    @Override
    public void start() {
        consoleInputChecker.startInputCheck();
    }

    @Override
    public void showMessageAboutSuccessConnect() {
        System.out.println("Успешное подключение");
    }

    @Override
    public void showMessageAboutFailedConnect(@NotNull String cause) {
        System.out.println("Ошибка подключения: " + cause);
    }

    @Override
    public void showGeneralMessage(@NotNull GeneralMessage message) {
        String dateStr = dateFormat.format(message.getSendTime());
        String userStr = message.getSender().getName();
        System.out.println(String.format(GENERAL_MESSAGE_OUTPUT_FORMAT, dateStr, userStr, message.getContent()));
    }
}
