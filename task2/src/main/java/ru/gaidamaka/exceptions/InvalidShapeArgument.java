package ru.gaidamaka.exceptions;


import ru.gaidamaka.ShapeType;

public class InvalidShapeArgument extends Exception{
    private final ShapeType shapeType;

    public InvalidShapeArgument(ShapeType shapeType, String errorMessage){
        super(errorMessage);
        this.shapeType = shapeType;
    }

    public InvalidShapeArgument(ShapeType shapeType, String errorMessage, Throwable cause){
        super(errorMessage, cause);
        this.shapeType = shapeType;
    }

    public InvalidShapeArgument(ShapeType shapeType, Throwable cause){
        super(cause);
        this.shapeType = shapeType;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }
}
