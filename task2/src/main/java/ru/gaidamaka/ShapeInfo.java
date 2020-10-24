package ru.gaidamaka;

import java.util.List;

public class ShapeInfo {
    private final String shapeName;
    private final List<Double> shapeArgs;

    public ShapeInfo(String shapeName, List<Double> shapeArgs) {
        this.shapeName = shapeName;
        this.shapeArgs = shapeArgs;
    }

    public List<Double> getArgs() {
        return shapeArgs;
    }

    public String getName() {
        return shapeName;
    }
}
