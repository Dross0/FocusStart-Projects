package ru.gaidamaka.squares;


import ru.gaidamaka.Shape;
import ru.gaidamaka.ShapeCharacteristic;
import ru.gaidamaka.ShapeType;
import ru.gaidamaka.UnitType;
import ru.gaidamaka.exceptions.InvalidShapeArgument;

import java.util.ArrayList;

public class Rectangle implements Shape {
    private final double a;
    private final double b;

    private ArrayList<ShapeCharacteristic> additionalInfo;

    public Rectangle(double a, double b) throws InvalidShapeArgument {
        if (!validateSideSizes(a, b)) {
            throw new InvalidShapeArgument(ShapeType.RECTANGLE, "Side sizes must be finite and > 0");
        }
        this.a = a;
        this.b = b;
    }

    private boolean validateSideSizes(double a, double b){
        return Double.isFinite(a) && Double.isFinite(b) && a > 0 && b > 0;
    }

    @Override
    public ShapeType getType() {
        return ShapeType.RECTANGLE;
    }

    @Override
    public double getSquare() {
        return a * b;
    }

    @Override
    public double getPerimeter() {
        return 2 * (a + b);
    }

    public double getDiagonalSize(){
        return Math.sqrt(a * a + b * b);
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    /**
     *
     * @return Длину (размер наибольшей стороны)
     */
    public double getLength(){
        return Math.max(a, b);
    }

    /**
     *
     * @return Ширину (размер наименьшей стороны)
     */
    public double getWidth(){
        return Math.min(a, b);
    }

    @Override
    public ArrayList<ShapeCharacteristic> getAdditionalInfo() {
        if (additionalInfo == null){
            additionalInfo = new ArrayList<>();
            additionalInfo.add(new ShapeCharacteristic("Длина диагонали", getDiagonalSize(), UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Длина", getLength(), UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Ширина", getWidth(), UnitType.LENGTH));
        }
        return additionalInfo;
    }
}
