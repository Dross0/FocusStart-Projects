package ru.gaidamaka.presenter;


import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.userevent.UserEvent;

public interface Presenter {
    void onEvent(@NotNull UserEvent eventInfo);
}
