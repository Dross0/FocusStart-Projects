package ru.gaidamaka.exception;

public class ShapeOutputException extends ShapeException{
    public ShapeOutputException(String errorMessage){
        super(errorMessage);
    }

    public ShapeOutputException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public ShapeOutputException(Throwable cause){
        super(cause);
    }
}
