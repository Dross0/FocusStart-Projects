package ru.gaidamaka.server;


public class ServerMain {
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server(2048);
        Thread serverThread = new Thread(server);
        serverThread.start();
        serverThread.join();
    }
}
