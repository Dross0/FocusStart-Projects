package ru.gaidamaka;

import ru.gaidamaka.game.event.GameEvent;

public interface GameObserver {
    void update(GameEvent gameEvent);
}
