package ru.gaidamaka.server.exception;

public class InvalidUserNameException extends RuntimeException {
    public InvalidUserNameException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidUserNameException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public InvalidUserNameException(Throwable cause) {
        super(cause);
    }
}
