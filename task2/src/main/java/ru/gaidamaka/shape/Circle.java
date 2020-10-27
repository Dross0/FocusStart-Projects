package ru.gaidamaka.shape;


import ru.gaidamaka.ShapeCharacteristic;
import ru.gaidamaka.ShapeType;
import ru.gaidamaka.UnitType;
import ru.gaidamaka.exception.InvalidShapeArgumentException;


import java.util.ArrayList;
import java.util.List;

public class Circle implements Shape {
    private final double radius;
    private List<ShapeCharacteristic> additionalInfo;

    public Circle(double radius) throws InvalidShapeArgumentException {
        validateRadius(radius);
        this.radius = radius;
    }

    private void validateRadius(double radius) throws InvalidShapeArgumentException {
        if (!Double.isFinite(radius) || radius <= 0){
            throw new InvalidShapeArgumentException(ShapeType.CIRCLE, "Radius must be finite and > 0");
        }
    }

    public double getRadius() {
        return radius;
    }

    public double getDiameter(){
        return 2 * radius;
    }

    @Override
    public ShapeType getType() {
        return ShapeType.CIRCLE;
    }

    @Override
    public double getSquare() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }


    @Override
    public List<ShapeCharacteristic> getAdditionalInfo() {
        if (additionalInfo == null){
            additionalInfo = new ArrayList<>();
            additionalInfo.add(new ShapeCharacteristic("Радиус", getRadius(), UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Диаметр", getDiameter(), UnitType.LENGTH));
        }
        return additionalInfo;
    }
}
