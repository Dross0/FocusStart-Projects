package ru.gaidamaka.exception;


import ru.gaidamaka.ShapeType;

public class InvalidShapeArgumentException extends ShapeException{
    private final ShapeType shapeType;

    public InvalidShapeArgumentException(ShapeType shapeType, String errorMessage){
        super(errorMessage);
        this.shapeType = shapeType;
    }

    public InvalidShapeArgumentException(ShapeType shapeType, String errorMessage, Throwable cause){
        super(errorMessage, cause);
        this.shapeType = shapeType;
    }

    public InvalidShapeArgumentException(ShapeType shapeType, Throwable cause){
        super(cause);
        this.shapeType = shapeType;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }
}
