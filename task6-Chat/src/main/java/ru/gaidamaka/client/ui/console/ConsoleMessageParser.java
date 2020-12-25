package ru.gaidamaka.client.ui.console;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.protocol.message.CommandCode;

import java.util.List;
import java.util.Optional;

public class ConsoleMessageParser {
    private static final String CONNECT_COMMAND = "/CONNECT";

    boolean isConnectMessage(@NotNull String rawMessage) {
        return rawMessage.startsWith(CONNECT_COMMAND);
    }

    @NotNull
    String parseUserNameFromLoginMessage(@NotNull String rawMessage) {
        String[] splitCommandCode = rawMessage.split(" ", 2);
        if (splitCommandCode.length != 2) {
            throw new IllegalArgumentException("Login message must contain: /LOGIN userName");
        }
        return splitCommandCode[1].strip();
    }

    @NotNull
    Optional<CommandCode> parseRequestCode(@NotNull String rawMessage) {
        for (CommandCode commandCode : CommandCode.values()) {
            if (rawMessage.startsWith("/" + commandCode)) {
                return Optional.of(commandCode);
            }
        }
        return Optional.empty();
    }

    @NotNull
    List<String> parseIPAndPortFromConnectMessage(@NotNull String rawMessage) {
        if (!isConnectMessage(rawMessage)) {
            throw new IllegalArgumentException("Is not connect message");
        }
        String[] splitConnectMessage = rawMessage.split(" ", 3);
        if (splitConnectMessage.length != 3) {
            throw new IllegalArgumentException("Connect message must contain: /CONNECT ip port");
        }
        return List.of(splitConnectMessage);
    }
}
