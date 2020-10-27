package ru.gaidamaka.exception;

public class ShapeFactoryParamsException extends ShapeException{
    public ShapeFactoryParamsException(String errorMessage){
        super(errorMessage);
    }

    public ShapeFactoryParamsException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public ShapeFactoryParamsException(Throwable cause){
        super(cause);
    }
}