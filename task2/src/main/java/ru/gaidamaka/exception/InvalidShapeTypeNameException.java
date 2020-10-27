package ru.gaidamaka.exception;


public class InvalidShapeTypeNameException extends ShapeException{
    public InvalidShapeTypeNameException(String errorMessage){
        super(errorMessage);
    }

    public InvalidShapeTypeNameException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public InvalidShapeTypeNameException(Throwable cause){
        super(cause);
    }
}