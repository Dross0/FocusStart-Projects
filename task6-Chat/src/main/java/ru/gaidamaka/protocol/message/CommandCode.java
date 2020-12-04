package ru.gaidamaka.protocol.message;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public enum CommandCode {
    EXIT("/exit", false),
    USER_LIST("/usersList", true),
    LOGIN("/login", true);

    @NotNull
    private final String commandStringRepresentation;
    private final boolean needResponse;

    CommandCode(@NotNull String commandStr, boolean isNeedResponse) {
        this.commandStringRepresentation = Objects.requireNonNull(commandStr, "Command string");
        this.needResponse = isNeedResponse;
    }

    @NotNull
    public String getCommandStringRepresentation() {
        return commandStringRepresentation;
    }

    public boolean isNeedResponse() {
        return needResponse;
    }
}
