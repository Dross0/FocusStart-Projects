package ru.gaidamaka.exception;

public class HighScoreTableManagerException extends RuntimeException {
    public HighScoreTableManagerException(String errorMessage) {
        super(errorMessage);
    }

    public HighScoreTableManagerException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public HighScoreTableManagerException(Throwable cause) {
        super(cause);
    }
}