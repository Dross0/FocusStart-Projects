package ru.gaidamaka.presenter;


import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.GameObserver;
import ru.gaidamaka.timer.TimerObserver;
import ru.gaidamaka.userevent.UserEvent;

public interface Presenter extends GameObserver, TimerObserver {
    void onEvent(@NotNull UserEvent eventInfo);
}
