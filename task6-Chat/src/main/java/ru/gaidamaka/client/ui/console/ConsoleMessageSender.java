package ru.gaidamaka.client.ui.console;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.client.presenter.ClientPresenter;
import ru.gaidamaka.protocol.message.CommandCode;

import java.util.List;
import java.util.Objects;

public class ConsoleMessageSender {
    @NotNull
    private final ConsoleMessageParser parser;
    private ClientPresenter clientPresenter;

    ConsoleMessageSender(@NotNull ConsoleMessageParser parser) {
        this.parser = Objects.requireNonNull(parser, "Parser cant be null");
    }

    void setClientPresenter(@NotNull ClientPresenter clientPresenter) {
        this.clientPresenter = Objects.requireNonNull(clientPresenter, "Presenter cant be null");
    }

    void send(@NotNull String rawMessage) {
        Objects.requireNonNull(rawMessage, "Message cant be null");
        validatePresenter();
        try {
            if (parser.isConnectMessage(rawMessage)) {
                sendConnect(rawMessage);
                return;
            }
            parser.parseRequestCode(rawMessage).ifPresentOrElse(code -> {
                        if (code == CommandCode.LOGIN) {
                            clientPresenter.onRequestMessageEvent(
                                    code,
                                    parser.parseUserNameFromLoginMessage(rawMessage)
                            );
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

    private void validatePresenter() {
        if (clientPresenter == null) {
            throw new IllegalStateException("No presenter");
        }
    }

    private void sendConnect(String rawMessage) {
        List<String> ipAndPort = parser.parseIPAndPortFromConnectMessage(rawMessage);
        try {
            clientPresenter.onConnectEvent(ipAndPort.get(1), Integer.parseInt(ipAndPort.get(2)));
        } catch (NumberFormatException e) {
            System.out.println("Port must be is integer");
        }
    }
}
