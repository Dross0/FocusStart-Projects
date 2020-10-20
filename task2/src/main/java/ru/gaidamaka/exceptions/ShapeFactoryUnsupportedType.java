package ru.gaidamaka.exceptions;

public class ShapeFactoryUnsupportedType extends Exception{
    public ShapeFactoryUnsupportedType(String errorMessage){
        super(errorMessage);
    }

    public ShapeFactoryUnsupportedType(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public ShapeFactoryUnsupportedType(Throwable cause){
        super(cause);
    }
}