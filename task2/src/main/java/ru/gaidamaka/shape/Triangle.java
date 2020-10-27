package ru.gaidamaka.shape;

import ru.gaidamaka.ShapeCharacteristic;
import ru.gaidamaka.ShapeType;
import ru.gaidamaka.UnitType;
import ru.gaidamaka.exception.InvalidShapeArgumentException;

import java.util.ArrayList;
import java.util.List;


public class Triangle implements Shape {
    private final double firstSide;
    private final double secondSide;
    private final double thirdSide;

    private List<ShapeCharacteristic> additionalInfo;

    public Triangle(double firstSide, double secondSide, double thirdSide) throws InvalidShapeArgumentException {
        validateSideSizes(firstSide, secondSide, thirdSide);
        this.firstSide = firstSide;
        this.secondSide = secondSide;
        this.thirdSide = thirdSide;
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
        return Math.sqrt(semiPerimeter * (semiPerimeter - firstSide) * (semiPerimeter - secondSide) * (semiPerimeter - thirdSide));
    }

    @Override
    public double getPerimeter() {
        return firstSide + secondSide + thirdSide;
    }

    public double getFirstSide() {
        return firstSide;
    }

    public double getSecondSide() {
        return secondSide;
    }

    public double getThirdSide() {
        return thirdSide;
    }

    private double getOppositeAngle(double side1, double side2, double oppositeSide) {
        double numerator = (side1 * side1) + (side2 * side2) - (oppositeSide * oppositeSide);
        double denominator = 2 * side1 * side2;
        return Math.acos(numerator / denominator) * 180 / Math.PI;
    }

    public double getFirstSideOppositeAngle() {
        return getOppositeAngle(secondSide, thirdSide, firstSide);
    }

    public double getSecondSideOppositeAngle() {
        return getOppositeAngle(firstSide, thirdSide, secondSide);
    }

    public double getThirdSideOppositeAngle() {
        return getOppositeAngle(firstSide, secondSide, thirdSide);
    }

    @Override
    public List<ShapeCharacteristic> getAdditionalInfo() {
        if (additionalInfo == null) {
            additionalInfo = new ArrayList<>();
            additionalInfo.add(new ShapeCharacteristic("Длина стороны A", firstSide, UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Длина стороны B", secondSide, UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Длина стороны C", thirdSide, UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Угол противолежащий стороне A", getFirstSideOppositeAngle(), UnitType.ANGLE));
            additionalInfo.add(new ShapeCharacteristic("Угол противолежащий стороне B", getSecondSideOppositeAngle(), UnitType.ANGLE));
            additionalInfo.add(new ShapeCharacteristic("Угол противолежащий стороне C", getThirdSideOppositeAngle(), UnitType.ANGLE));
        }
        return additionalInfo;
    }
}
