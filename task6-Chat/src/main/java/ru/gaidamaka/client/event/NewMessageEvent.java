package ru.gaidamaka.client.event;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.protocol.message.Message;

public class NewMessageEvent extends ClientEvent {
    @NotNull
    private final Message message;

    public NewMessageEvent(@NotNull Message message) {
        super(EventType.NEW_MESSAGE);
        this.message = message;
    }

    @NotNull
    public Message getMessage() {
        return message;
    }
}
