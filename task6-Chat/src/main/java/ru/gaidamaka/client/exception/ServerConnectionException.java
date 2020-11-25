package ru.gaidamaka.client.exception;

public class ServerConnectionException extends RuntimeException {
    public ServerConnectionException(String errorMessage) {
        super(errorMessage);
    }

    public ServerConnectionException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public ServerConnectionException(Throwable cause) {
        super(cause);
    }
}