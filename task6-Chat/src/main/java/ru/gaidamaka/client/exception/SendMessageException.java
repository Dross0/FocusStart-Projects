package ru.gaidamaka.client.exception;

public class SendMessageException extends RuntimeException {
    public SendMessageException(String errorMessage) {
        super(errorMessage);
    }

    public SendMessageException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public SendMessageException(Throwable cause) {
        super(cause);
    }
}