package ru.gaidamaka.exceptions;

public class WrongTableSizeException extends Exception{
    public WrongTableSizeException(final String errorMessage){
        super(errorMessage);
    }
}
