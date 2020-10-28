package ru.gaidamaka.exception;

public class ShapeFactoryException extends ShapeException{
    public ShapeFactoryException(String errorMessage){
        super(errorMessage);
    }

    public ShapeFactoryException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public ShapeFactoryException(Throwable cause){
        super(cause);
    }
}