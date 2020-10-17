package ru.gaidamaka.squares;


import ru.gaidamaka.Shape;
import ru.gaidamaka.ShapeCharacteristic;
import ru.gaidamaka.ShapeType;
import ru.gaidamaka.UnitType;
import ru.gaidamaka.exceptions.InvalidShapeArgument;


import java.util.ArrayList;

public class Circle implements Shape {
    private final double radius;
    private ArrayList<ShapeCharacteristic> additionalInfo;

    public Circle(double radius) throws InvalidShapeArgument {
        if (!validateRadius(radius)) {
            throw new InvalidShapeArgument(ShapeType.CIRCLE, "Radius must be finite and > 0");
        }
        this.radius = radius;
    }

    private boolean validateRadius(double radius){
        return Double.isFinite(radius) && radius > 0;
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
    public ArrayList<ShapeCharacteristic> getAdditionalInfo() {
        if (additionalInfo == null){
            additionalInfo = new ArrayList<>();
            additionalInfo.add(new ShapeCharacteristic("Радиус", getRadius(), UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Диаметр", getDiameter(), UnitType.LENGTH));
        }
        return additionalInfo;
    }
}
