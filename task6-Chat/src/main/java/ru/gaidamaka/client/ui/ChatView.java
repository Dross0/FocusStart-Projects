package ru.gaidamaka.client.ui;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.client.presenter.ClientPresenter;
import ru.gaidamaka.protocol.message.GeneralMessage;


public interface ChatView {
    void showMessageAboutSuccessConnect();

    void showMessageAboutFailedConnect(@NotNull String cause);

    void exit();

    void showError(@NotNull String errorMessage);

    void setClientPresenter(@NotNull ClientPresenter presenter);

    void showLoginFail(@NotNull String userName);

    void showUserList(@NotNull String userList);

    void start();

    void showGeneralMessage(@NotNull GeneralMessage message);
}
