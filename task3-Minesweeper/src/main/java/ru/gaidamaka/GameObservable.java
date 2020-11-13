package ru.gaidamaka;


import ru.gaidamaka.game.event.GameEvent;

public interface GameObservable {
    void addObserver(GameObserver gameObserver);

    void removeObserver(GameObserver gameObserver);

    void notifyObservers(GameEvent event);
}
