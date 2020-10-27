package ru.gaidamaka.exception;

public class ShapeFactoryUnsupportedTypeException extends ShapeException{
    public ShapeFactoryUnsupportedTypeException(String errorMessage){
        super(errorMessage);
    }

    public ShapeFactoryUnsupportedTypeException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public ShapeFactoryUnsupportedTypeException(Throwable cause){
        super(cause);
    }
}