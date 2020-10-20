package ru.gaidamaka.exceptions;

public class InvalidShapeTypeName extends Exception{
    public InvalidShapeTypeName(String errorMessage){
        super(errorMessage);
    }

    public InvalidShapeTypeName(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public InvalidShapeTypeName(Throwable cause){
        super(cause);
    }
}