package ru.gaidamaka.client;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.client.event.ClientEvent;

public interface ClientObserver {
    void update(@NotNull ClientEvent event);
}
