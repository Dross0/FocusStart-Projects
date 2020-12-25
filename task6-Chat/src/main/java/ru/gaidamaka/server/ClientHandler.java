package ru.gaidamaka.server;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.protocol.message.Message;

public interface ClientHandler {
    void sendMessage(@NotNull Message message);
}
