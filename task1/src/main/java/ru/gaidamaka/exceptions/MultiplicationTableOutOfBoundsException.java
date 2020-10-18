package ru.gaidamaka.exceptions;

public class MultiplicationTableOutOfBoundsException extends RuntimeException{
    public MultiplicationTableOutOfBoundsException(final String errorMessage){
        super(errorMessage);
    }
}