package ru.gaidamaka.exceptions;

public class MultiplicationTableOutOfBoundsException extends RuntimeException{
    public MultiplicationTableOutOfBoundsException(String errorMessage){
        super(errorMessage);
    }

    public MultiplicationTableOutOfBoundsException(Throwable cause){
        super(cause);
    }

    public MultiplicationTableOutOfBoundsException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }
}