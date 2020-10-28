package ru.gaidamaka;

public enum ShapeType {
    CIRCLE("Круг", 1),
    TRIANGLE("Треугольник", 3),
    RECTANGLE("Прямоугольник", 2);

    private final String shapeName;
    private final int paramsNumber;

    ShapeType(String shapeName, int paramsNumber) {
        this.shapeName = shapeName;
        this.paramsNumber = paramsNumber;
    }

    public int getParamsNumber() {
        return paramsNumber;
    }

    public String getShapeName() {
        return shapeName;
    }
}
