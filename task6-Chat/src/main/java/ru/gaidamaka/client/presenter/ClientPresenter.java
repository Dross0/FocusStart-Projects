package ru.gaidamaka.client.presenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.gaidamaka.protocol.message.CommandCode;

public interface ClientPresenter {
    void onGeneralMessageEvent(@NotNull String content);

    void onRequestMessageEvent(@NotNull CommandCode code, @Nullable String userName);

    void onConnectEvent(@NotNull String ipStr, int port);

    void start();
}
