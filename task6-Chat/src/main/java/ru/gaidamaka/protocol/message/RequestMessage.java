package ru.gaidamaka.protocol.message;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RequestMessage extends Message {
    @NotNull
    private final CommandCode commandCode;

    @NotNull
    private final User user;

    public RequestMessage(@NotNull CommandCode commandCode, @NotNull User user) {
        super(
                Objects.requireNonNull(commandCode, "Command code cant be null")
                        .getCommandStringRepresentation(),
                MessageType.SERVER_REQUEST
        );
        this.commandCode = commandCode;
        this.user = Objects.requireNonNull(user, "User cant be null");
    }

    @NotNull
    public CommandCode getCommandCode() {
        return commandCode;
    }

    @NotNull
    public User getUser() {
        return user;
    }
}
