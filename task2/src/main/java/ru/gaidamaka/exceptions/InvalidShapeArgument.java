package ru.gaidamaka.exceptions;

import ru.gaidamaka.ShapeType;

public class InvalidShapeArgument extends Exception{
    private final ShapeType shapeType;

    public InvalidShapeArgument(ShapeType shapeType, final String errorMessage){
        super(errorMessage);
        this.shapeType = shapeType;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }
}
