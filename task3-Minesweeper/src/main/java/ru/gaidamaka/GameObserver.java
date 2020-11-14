package ru.gaidamaka;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.game.event.GameEvent;

public interface GameObserver {
    void update(@NotNull GameEvent gameEvent);
}
