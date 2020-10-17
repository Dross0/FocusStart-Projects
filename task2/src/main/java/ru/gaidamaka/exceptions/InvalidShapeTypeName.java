package ru.gaidamaka.exceptions;

public class InvalidShapeTypeName extends Exception{
    public InvalidShapeTypeName(final String errorMessage){
        super(errorMessage);
    }
}
