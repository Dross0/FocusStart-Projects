package ru.gaidamaka.shape;


import ru.gaidamaka.ShapeCharacteristic;
import ru.gaidamaka.ShapeType;
import ru.gaidamaka.UnitType;
import ru.gaidamaka.exception.InvalidShapeArgumentException;

import java.util.ArrayList;
import java.util.List;

public class Rectangle implements Shape {
    private final double firstSide;
    private final double secondSide;

    private List<ShapeCharacteristic> additionalInfo;

    public Rectangle(double firstSide, double secondSide) throws InvalidShapeArgumentException {
        validateSideSizes(firstSide, secondSide);
        this.firstSide = firstSide;
        this.secondSide = secondSide;
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
        return firstSide * secondSide;
    }

    @Override
    public double getPerimeter() {
        return 2 * (firstSide + secondSide);
    }

    public double getDiagonalSize(){
        return Math.sqrt(firstSide * firstSide + secondSide * secondSide);
    }

    public double getFirstSide() {
        return firstSide;
    }

    public double getSecondSide() {
        return secondSide;
    }

    /**
     *
     * @return Длину (размер наибольшей стороны)
     */
    public double getLength(){
        return Math.max(firstSide, secondSide);
    }

    /**
     *
     * @return Ширину (размер наименьшей стороны)
     */
    public double getWidth(){
        return Math.min(firstSide, secondSide);
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
