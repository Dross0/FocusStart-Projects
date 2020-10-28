package ru.gaidamaka;

import ru.gaidamaka.exception.*;
import ru.gaidamaka.shape.Circle;
import ru.gaidamaka.shape.Rectangle;
import ru.gaidamaka.shape.Shape;
import ru.gaidamaka.shape.Triangle;

import java.util.List;

public final class ShapeFactory {
    private ShapeFactory() {}

    public static Shape createShape(ShapeType shapeType, List<Double> params) throws ShapeException {
        if (shapeType.getParamsNumber() != params.size()) {
            throw new ShapeFactoryException("Cant create " + shapeType + " because expected params number = "
                    + shapeType.getParamsNumber() + ", actual = " + params.size());
        }
        switch (shapeType) {
            case CIRCLE:
                return new Circle(params.get(0));
            case TRIANGLE:
                return new Triangle(params.get(0), params.get(1), params.get(2));
            case RECTANGLE:
                return new Rectangle(params.get(0), params.get(1));
            default:
                throw new ShapeFactoryException("Type = " + shapeType + " now is unsupported");
        }
    }

    public static Shape createShape(String shapeName, List<Double> params) throws ShapeException {
        try {
            ShapeType shapeType = ShapeType.valueOf(shapeName);
            return createShape(shapeType, params);
        } catch (IllegalArgumentException ex) {
            throw new ShapeFactoryException("Cant find {" + shapeName + "} shape type");
        }
    }

}
