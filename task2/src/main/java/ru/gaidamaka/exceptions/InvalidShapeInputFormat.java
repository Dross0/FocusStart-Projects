package ru.gaidamaka.exceptions;

public class InvalidShapeInputFormat extends Exception{
    public InvalidShapeInputFormat(final String errorMessage){
        super(errorMessage);
    }
}
