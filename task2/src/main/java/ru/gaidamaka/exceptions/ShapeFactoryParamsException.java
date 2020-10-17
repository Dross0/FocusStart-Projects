package ru.gaidamaka.exceptions;

public class ShapeFactoryParamsException extends Exception{
    public ShapeFactoryParamsException(final String errorMessage){
        super(errorMessage);
    }
}