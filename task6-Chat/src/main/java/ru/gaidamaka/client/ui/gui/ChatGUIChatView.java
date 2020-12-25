package ru.gaidamaka.client.ui.gui;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.client.presenter.ClientPresenter;
import ru.gaidamaka.client.ui.ChatView;
import ru.gaidamaka.protocol.message.CommandCode;
import ru.gaidamaka.protocol.message.GeneralMessage;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class ChatGUIChatView implements ChatView {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM HH:mm", Locale.getDefault());
    private static final Logger logger = LoggerFactory.getLogger(ChatGUIChatView.class);
    public static final String LOGIN_FAIL_OUTPUT_FORMAT = "Имя - %s уже занято";

    private final JPanel mainPane;
    private JButton userListButton;
    private JButton sendButton;
    private JTextPane messageInputField;
    private JTextPane messagesListPane;
    private ClientPresenter presenter;
    private JFrame mainWindow;
    private GeneralMessageStyle generalMessageStyle;

    public ChatGUIChatView() {
        mainPane = new JPanel();
        mainPane.setLayout(new GridBagLayout());
        initUserListButton();
        initSendButton();
        initMessageInputField();
        initMessagesListArea();
        arrangeAllElements(userListButton, sendButton, messageInputField, messagesListPane);
    }

    @Override
    public void start() {
        mainWindow = new JFrame("Chat");
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setContentPane(mainPane);
        mainWindow.pack();
        showConnectionDialog();
        mainWindow.setVisible(true);
    }

    private void showConnectionDialog() {
        String ip = JOptionPane.showInputDialog("Введите ip");
        String portStr = JOptionPane.showInputDialog("Введите порт");
        try {
            presenter.onConnectEvent(ip, Integer.parseInt(portStr));
        } catch (NumberFormatException e) {
            showError("Port must be a number");
            showConnectionDialog();
        }
    }

    private void showLoginDialog() {
        String userName = JOptionPane.showInputDialog("Введите имя");
        presenter.onRequestMessageEvent(CommandCode.LOGIN, userName);
    }

    private void arrangeAllElements(JButton userListButton, JButton sendButton, JTextPane messageInputField, JTextPane messagesListArea) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPane.add(userListButton, gbc);

        gbc.gridx = 1;
        mainPane.add(sendButton, gbc);

        final JScrollPane messageInputScrollPane = new JScrollPane();
        messageInputScrollPane.setHorizontalScrollBarPolicy(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 10;
        gbc.insets = new Insets(15, 0, 0, 0);
        mainPane.add(messageInputScrollPane, gbc);
        messageInputScrollPane.setViewportView(messageInputField);

        final JScrollPane messagesListScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 100;
        mainPane.add(messagesListScrollPane, gbc);
        messagesListScrollPane.setViewportView(messagesListArea);
    }

    private void initMessagesListArea() {
        messagesListPane = new JTextPane();
        messagesListPane.setEditable(false);
        generalMessageStyle = new GeneralMessageStyle(messagesListPane);
    }

    private void initMessageInputField() {
        messageInputField = new JTextPane();
    }


    private void initSendButton() {
        sendButton = new JButton("Отправить");
        sendButton.addActionListener(e -> {
            presenter.onGeneralMessageEvent(messageInputField.getText());
            messageInputField.setText("");
        });
    }

    private void initUserListButton() {
        userListButton = new JButton("Список пользователей");
        userListButton.addActionListener(e -> presenter.onRequestMessageEvent(CommandCode.USER_LIST));
    }


    @Override
    public void showGeneralMessage(@NotNull GeneralMessage message) {
        String dateStr = dateFormat.format(message.getSendTime());
        String userStr = message.getSender().getName();
        StyledDocument doc = messagesListPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), dateStr, generalMessageStyle.getDateStyle());
            doc.insertString(doc.getLength(), " " + userStr, generalMessageStyle.getUserNameStyle());
            doc.insertString(doc.getLength(), ": " + message.getContent() + "\n", generalMessageStyle.getContentStyle());
        } catch (BadLocationException e) {
            logger.error("Error while general message output", e);
        }
    }


    @Override
    public void showMessageAboutSuccessConnect() {
        showLoginDialog();
    }

    @Override
    public void exit() {
        mainWindow.setVisible(false);
        mainWindow.dispatchEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void showError(@NotNull String errorMessage) {
        JOptionPane.showMessageDialog(mainPane, errorMessage, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void setClientPresenter(@NotNull ClientPresenter presenter) {
        this.presenter = Objects.requireNonNull(presenter);
    }

    @Override
    public void showLoginFail(@NotNull String userName) {
        JOptionPane.showMessageDialog(
                mainPane,
                String.format(LOGIN_FAIL_OUTPUT_FORMAT, userName),
                "Ошибка входа",
                JOptionPane.ERROR_MESSAGE
        );
        showLoginDialog();
    }

    @Override
    public void showUserList(@NotNull String userList) {
        JOptionPane.showMessageDialog(
                mainPane,
                userList,
                "Список пользователей",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void showMessageAboutFailedConnect(@NotNull String cause) {
        JOptionPane.showMessageDialog(
                mainPane,
                cause,
                "Ошибка подключения",
                JOptionPane.ERROR_MESSAGE
        );
        showConnectionDialog();
    }
}
