package ru.gaidamaka;

import ru.gaidamaka.exceptions.InvalidShapeArgument;
import ru.gaidamaka.exceptions.InvalidShapeTypeName;
import ru.gaidamaka.exceptions.ShapeFactoryParamsException;
import ru.gaidamaka.exceptions.ShapeFactoryUnsupportedType;
import ru.gaidamaka.squares.Circle;
import ru.gaidamaka.squares.Rectangle;
import ru.gaidamaka.squares.Triangle;

import java.util.ArrayList;

public class ShapeFactory {
    public static Shape getSquare(ShapeType shapeType, ArrayList<Double> params) throws ShapeFactoryParamsException, InvalidShapeArgument, ShapeFactoryUnsupportedType {
        if (shapeType.getParamsNumber() != params.size()){
            throw new ShapeFactoryParamsException("Cant create " + shapeType + " because expected params number = "
                    + shapeType.getParamsNumber() + ", actual = " + params.size());
        }
        switch (shapeType){
            case CIRCLE:
                return new Circle(params.get(0));
            case TRIANGLE:
                return new Triangle(params.get(0), params.get(1), params.get(2));
            case RECTANGLE:
                return new Rectangle(params.get(0), params.get(1));
            default:
                throw new ShapeFactoryUnsupportedType("Type = " + shapeType + " now is unsupported");
        }
    }

    public static Shape getSquare(String shapeName, ArrayList<Double> params) throws InvalidShapeTypeName, ShapeFactoryUnsupportedType, ShapeFactoryParamsException, InvalidShapeArgument {
        try {
            ShapeType shapeType = ShapeType.valueOf(shapeName);
            return ShapeFactory.getSquare(shapeType, params);
        }
        catch (IllegalArgumentException | NullPointerException ex){
            throw new InvalidShapeTypeName("Cant find {" + shapeName + "} shape type");
        }
    }

}
