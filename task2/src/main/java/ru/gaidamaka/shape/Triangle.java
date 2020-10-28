package ru.gaidamaka.shape;

import ru.gaidamaka.ShapeCharacteristic;
import ru.gaidamaka.ShapeType;
import ru.gaidamaka.UnitType;
import ru.gaidamaka.exception.InvalidShapeArgumentException;

import java.util.ArrayList;
import java.util.List;


public class Triangle implements Shape {
    private final double sideA;
    private final double sideB;
    private final double sideC;

    private List<ShapeCharacteristic> additionalInfo;

    public Triangle(double sideA, double sideB, double sideC) throws InvalidShapeArgumentException {
        validateSideSizes(sideA, sideB, sideC);
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }

    private void validateSideSizes(double a, double b, double c) throws InvalidShapeArgumentException {
        boolean allSidesAreFinite = Double.isFinite(a) && Double.isFinite(b) && Double.isFinite(c);
        boolean allSidesArePositive = a > 0 && b > 0 && c > 0;
        boolean triangleConditionOnSides = a + b >= c && a + c >= b && b + c >= a;
        if (!allSidesAreFinite || !allSidesArePositive || !triangleConditionOnSides) {
            throw new InvalidShapeArgumentException(ShapeType.TRIANGLE, "Side sizes must be finite and > 0" +
                    " and the sum of any two parties must be greater than the third");
        }
    }

    @Override
    public ShapeType getType() {
        return ShapeType.TRIANGLE;
    }

    @Override
    public double getSquare() {
        double semiPerimeter = getPerimeter() / 2;
        return Math.sqrt(semiPerimeter * (semiPerimeter - sideA) * (semiPerimeter - sideB) * (semiPerimeter - sideC));
    }

    @Override
    public double getPerimeter() {
        return sideA + sideB + sideC;
    }

    public double getSideA() {
        return sideA;
    }

    public double getSideB() {
        return sideB;
    }

    public double getSideC() {
        return sideC;
    }

    private double getOppositeAngle(double side1, double side2, double oppositeSide) {
        double numerator = (side1 * side1) + (side2 * side2) - (oppositeSide * oppositeSide);
        double denominator = 2 * side1 * side2;
        return Math.acos(numerator / denominator) * 180 / Math.PI;
    }

    public double getSideAOppositeAngle() {
        return getOppositeAngle(sideB, sideC, sideA);
    }

    public double getSideBOppositeAngle() {
        return getOppositeAngle(sideA, sideC, sideB);
    }

    public double getSideCOppositeAngle() {
        return getOppositeAngle(sideA, sideB, sideC);
    }

    @Override
    public List<ShapeCharacteristic> getAdditionalInfo() {
        if (additionalInfo == null) {
            additionalInfo = new ArrayList<>();
            additionalInfo.add(new ShapeCharacteristic("Длина стороны A", sideA, UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Длина стороны B", sideB, UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Длина стороны C", sideC, UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Угол противолежащий стороне A", getSideAOppositeAngle(), UnitType.ANGLE));
            additionalInfo.add(new ShapeCharacteristic("Угол противолежащий стороне B", getSideBOppositeAngle(), UnitType.ANGLE));
            additionalInfo.add(new ShapeCharacteristic("Угол противолежащий стороне C", getSideCOppositeAngle(), UnitType.ANGLE));
        }
        return additionalInfo;
    }
}
