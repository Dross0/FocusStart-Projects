package ru.gaidamaka.exceptions;

public class ShapeFactoryUnsupportedType extends Exception{
    public ShapeFactoryUnsupportedType(final String errorMessage){
        super(errorMessage);
    }
}
