package ru.gaidamaka.presenter;


import ru.gaidamaka.GameObserver;
import ru.gaidamaka.timer.TimerObserver;
import ru.gaidamaka.userevent.UserEvent;

public interface Presenter extends GameObserver, TimerObserver {
    void onEvent(UserEvent eventInfo);
}
