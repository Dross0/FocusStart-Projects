package ru.gaidamaka.exceptions;

public class ShapeOutputException extends Exception{
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
