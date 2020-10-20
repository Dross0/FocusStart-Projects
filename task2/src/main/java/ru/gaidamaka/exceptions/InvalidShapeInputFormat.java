package ru.gaidamaka.exceptions;

public class InvalidShapeInputFormat extends Exception{
    public InvalidShapeInputFormat(String errorMessage){
        super(errorMessage);
    }

    public InvalidShapeInputFormat(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public InvalidShapeInputFormat(Throwable cause){
        super(cause);
    }
}