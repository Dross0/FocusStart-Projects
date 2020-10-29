package ru.gaidamaka.exception;

public class GameFieldException extends RuntimeException{
    public GameFieldException(String errorMessage){
        super(errorMessage);
    }

    public GameFieldException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public GameFieldException(Throwable cause){
        super(cause);
    }
}