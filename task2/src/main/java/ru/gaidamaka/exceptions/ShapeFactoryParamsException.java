package ru.gaidamaka.exceptions;

public class ShapeFactoryParamsException extends Exception{
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