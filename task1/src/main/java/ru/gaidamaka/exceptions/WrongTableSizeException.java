package ru.gaidamaka.exceptions;

public class WrongTableSizeException extends Exception{
    public WrongTableSizeException(String errorMessage){
        super(errorMessage);
    }
}
