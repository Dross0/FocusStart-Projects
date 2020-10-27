package ru.gaidamaka.exception;

public class ShapeException extends Exception{
    public ShapeException(String errorMessage){
        super(errorMessage);
    }

    public ShapeException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public ShapeException(Throwable cause){
        super(cause);
    }
}
