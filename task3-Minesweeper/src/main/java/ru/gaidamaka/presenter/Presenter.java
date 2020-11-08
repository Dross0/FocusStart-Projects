package ru.gaidamaka.presenter;

import ru.gaidamaka.GameObserver;
import ru.gaidamaka.UserEvent;
import ru.gaidamaka.timer.TimerObserver;

public interface Presenter extends GameObserver, TimerObserver {
    void onEvent(UserEvent eventInfo);
}
