package ru.gaidamaka.protocol.message;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class GeneralMessage extends Message {
    @NotNull
    private final User sender;

    @NotNull
    private final LocalDateTime sendTime;

    public GeneralMessage(@NotNull String content, @NotNull User sender) {
        super(content, MessageType.GENERAL_MESSAGE);
        this.sender = Objects.requireNonNull(sender, "Sender cant be null");
        this.sendTime = LocalDateTime.now();
    }

    @NotNull
    public LocalDateTime getSendTime() {
        return sendTime;
    }

    @NotNull
    public User getSender() {
        return sender;
    }
}
