package ru.gaidamaka.protocol.message;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public abstract class Message implements Serializable {
    @NotNull
    private final String content;

    @NotNull
    private final MessageType type;

    public Message(@NotNull String content, @NotNull MessageType type) {
        this.content = Objects.requireNonNull(content, "Message content cant be null");
        this.type = Objects.requireNonNull(type, "Message type cant be null");
    }

    public @NotNull String getContent() {
        return content;
    }

    public @NotNull MessageType getType() {
        return type;
    }
}
