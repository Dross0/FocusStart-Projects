package ru.gaidamaka.client.ui.gui;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.client.presenter.ClientPresenter;
import ru.gaidamaka.client.ui.ChatView;
import ru.gaidamaka.protocol.message.Message;

public class ChatGUIChatView implements ChatView {
    @Override
    public void showMessage(@NotNull Message message) {

    }

    @Override
    public void showMessageAboutSuccessConnect() {

    }

    @Override
    public void showMessageAboutFailedConnect() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void showError(@NotNull String errorMessage) {

    }

    @Override
    public void setClientPresenter(@NotNull ClientPresenter presenter) {

    }
}
