package ru.gaidamaka.client.ui;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.client.presenter.ClientPresenter;
import ru.gaidamaka.protocol.message.Message;

public interface ChatView {
    void showMessage(@NotNull Message message);

    void showMessageAboutSuccessConnect();

    void showMessageAboutFailedConnect();

    void exit();

    void showError(@NotNull String errorMessage);

    void setClientPresenter(@NotNull ClientPresenter presenter);
}
