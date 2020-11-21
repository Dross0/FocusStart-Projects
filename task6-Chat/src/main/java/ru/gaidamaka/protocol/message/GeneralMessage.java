package ru.gaidamaka.protocol.message;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class GeneralMessage extends Message {
    @NotNull
    private final User sender;

    @NotNull
    private final Calendar sendTime;

    public GeneralMessage(@NotNull String content, @NotNull User sender) {
        super(content, MessageType.GENERAL_MESSAGE);
        this.sender = Objects.requireNonNull(sender, "Sender cant be null");
        this.sendTime = new GregorianCalendar();
    }

    @NotNull
    public Calendar getSendTime() {
        return sendTime;
    }

    @NotNull
    public User getSender() {
        return sender;
    }
}
