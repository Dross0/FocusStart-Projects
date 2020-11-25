package ru.gaidamaka.client;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.client.event.ClientEvent;

public interface ClientObservable {
    void notifyObservers(@NotNull ClientEvent event);

    void addObserver(@NotNull ClientObserver clientObserver);

    void removeObserver(@NotNull ClientObserver clientObserver);
}
