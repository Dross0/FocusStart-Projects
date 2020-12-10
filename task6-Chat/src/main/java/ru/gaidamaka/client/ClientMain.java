package ru.gaidamaka.client;

import ru.gaidamaka.client.presenter.ChatClientPresenter;
import ru.gaidamaka.client.presenter.ClientPresenter;
import ru.gaidamaka.client.ui.ChatView;
import ru.gaidamaka.client.ui.gui.ChatGUIChatView;


public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client();
        ChatView view = new ChatGUIChatView();
        ClientPresenter presenter = new ChatClientPresenter(client, view);
        view.setClientPresenter(presenter);
        view.start();
        presenter.start();
    }
}
