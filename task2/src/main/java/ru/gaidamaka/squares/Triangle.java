package ru.gaidamaka.squares;

import ru.gaidamaka.Shape;
import ru.gaidamaka.ShapeCharacteristic;
import ru.gaidamaka.ShapeType;
import ru.gaidamaka.UnitType;
import ru.gaidamaka.exceptions.InvalidShapeArgument;

import java.util.ArrayList;


public class Triangle implements Shape {
    private final double a;
    private final double b;
    private final double c;

    private ArrayList<ShapeCharacteristic> additionalInfo;

    public Triangle(double a, double b, double c) throws InvalidShapeArgument {
        if (!validateSideSizes(a, b, c)) {
            throw new InvalidShapeArgument(ShapeType.TRIANGLE, "Side sizes must be finite and > 0" +
                    " and the sum of any two parties must be greater than the third");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    private boolean validateSideSizes(double a, double b, double c){
        return Double.isFinite(a) && Double.isFinite(b) && Double.isFinite(c)
                && a > 0 && b > 0 && c > 0
                && a + b > c && a + c > b && b + c > a;
    }

    @Override
    public ShapeType getType() {
        return ShapeType.TRIANGLE;
    }

    @Override
    public double getSquare() {
        double semiPerimeter = getPerimeter() / 2;
        return Math.sqrt(semiPerimeter * (semiPerimeter - a) * (semiPerimeter - b) * (semiPerimeter - c));
    }

    @Override
    public double getPerimeter() {
        return a + b + c;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    private double getOppositeAngle(double side1, double side2, double oppositeSide){
        double numerator = (side1 * side1) + (side2 * side2) - (oppositeSide * oppositeSide);
        double denominator = 2 * side1 * side2;
        return Math.acos(numerator / denominator) * 180 / Math.PI;
    }

    public double getAOppositeAngle(){
        return getOppositeAngle(b, c, a);
    }

    public double getBOppositeAngle(){
        return getOppositeAngle(a, c, b);
    }

    public double getCOppositeAngle(){
        return getOppositeAngle(a, b, c);
    }

    @Override
    public ArrayList<ShapeCharacteristic> getAdditionalInfo() {
        if (additionalInfo == null){
            additionalInfo = new ArrayList<>();
            additionalInfo.add(new ShapeCharacteristic("Длина стороны A", a, UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Длина стороны B", b, UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Длина стороны C", c, UnitType.LENGTH));
            additionalInfo.add(new ShapeCharacteristic("Угол противолежащий стороне A", getAOppositeAngle(), UnitType.ANGLE));
            additionalInfo.add(new ShapeCharacteristic("Угол противолежащий стороне B", getBOppositeAngle(), UnitType.ANGLE));
            additionalInfo.add(new ShapeCharacteristic("Угол противолежащий стороне C", getCOppositeAngle(), UnitType.ANGLE));
        }
        return additionalInfo;
    }
}
