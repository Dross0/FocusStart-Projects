package ru.gaidamaka.exception;

public class ImageReadException extends RuntimeException {
    public ImageReadException(String errorMessage) {
        super(errorMessage);
    }

    public ImageReadException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public ImageReadException(Throwable cause) {
        super(cause);
    }
}
