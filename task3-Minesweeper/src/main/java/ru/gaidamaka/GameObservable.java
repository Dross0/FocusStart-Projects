package ru.gaidamaka;


import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.game.event.GameEvent;

public interface GameObservable {
    void addObserver(@NotNull GameObserver gameObserver);

    void removeObserver(@NotNull GameObserver gameObserver);

    void notifyObservers(@NotNull GameEvent event);
}
