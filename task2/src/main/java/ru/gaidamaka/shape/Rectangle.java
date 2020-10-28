package ru.gaidamaka.shape;


import ru.gaidamaka.ShapeCharacteristic;
import ru.gaidamaka.ShapeType;
import ru.gaidamaka.UnitType;
import ru.gaidamaka.exception.InvalidShapeArgumentException;

import java.util.ArrayList;
import java.util.List;

public class Rectangle implements Shape {
    private final double sideA;
    private final double sideB;

    private List<ShapeCharacteristic> additionalInfo;

    public Rectangle(double sideA, double sideB) throws InvalidShapeArgumentException {
        validateSideSizes(sideA, sideB);
        this.sideA = sideA;
        this.sideB = sideB;
    }

    private void validateSideSizes(double a, double b) throws InvalidShapeArgumentException {
        if (!Double.isFinite(a) || !Double.isFinite(b) || a <= 0 || b <= 0){
            throw new InvalidShapeArgumentException(ShapeType.RECTANGLE, "Side sizes must be finite and > 0");
        }
    }

    @Override
    public ShapeType getType() {
        return ShapeType.RECTANGLE;
    }

    @Override
    public double getSquare() {
        return sideA * sideB;
    }

    @Override
    public double getPerimeter() {
        return 2 * (sideA + sideB);
    }

    public double getDiagonalSize(){
        return Math.sqrt(sideA * sideA + sideB * sideB);
    }

    public double getSideA() {
        return sideA;
    }

    public double getSideB() {
        return sideB;
    }

    /**
     *
     * @return Длину (размер наибольшей стороны)
     */
    public double getLength(){
        return Math.max(sideA, sideB);
    }

    /**
     *
     * @return Ширину (размер наименьшей стороны)
     */
    public double getWidth(){
        return Math.min(sideA, sideB);
    }

    @Override
    public List<ShapeCharacteristic> getAdditionalInfo() {
        if (additionalInfo == null){
            additionalInfo = new ArrayList<>();
            additionalInfo.add(new ShapeCharacteristic("Длина диагонали", getDiagonalSize(), UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Длина", getLength(), UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Ширина", getWidth(), UnitType.LENGTH));
        }
        return additionalInfo;
    }
}
