package ru.gaidamaka.client;

import ru.gaidamaka.client.presenter.ClientPresenter;
import ru.gaidamaka.client.presenter.ConsolePresenter;
import ru.gaidamaka.client.ui.ChatView;
import ru.gaidamaka.client.ui.console.ConsoleChatView;

public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client();
        ChatView view = new ConsoleChatView();
        ClientPresenter presenter = new ConsolePresenter(client, view);
        presenter.start();
    }
}
