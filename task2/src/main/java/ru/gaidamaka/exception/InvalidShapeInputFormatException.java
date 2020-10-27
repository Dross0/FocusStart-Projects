package ru.gaidamaka.exception;

public class InvalidShapeInputFormatException extends ShapeException{
    public InvalidShapeInputFormatException(String errorMessage){
        super(errorMessage);
    }

    public InvalidShapeInputFormatException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public InvalidShapeInputFormatException(Throwable cause){
        super(cause);
    }
}