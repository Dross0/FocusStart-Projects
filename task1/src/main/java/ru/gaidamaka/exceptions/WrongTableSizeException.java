package ru.gaidamaka.exceptions;

public class WrongTableSizeException extends Exception{
    public WrongTableSizeException(String errorMessage){
        super(errorMessage);
    }

    public WrongTableSizeException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public WrongTableSizeException(Throwable cause){
        super(cause);
    }
}
