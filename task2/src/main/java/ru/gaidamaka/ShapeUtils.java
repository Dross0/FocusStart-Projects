package ru.gaidamaka;

import ru.gaidamaka.exception.InvalidShapeInputFormatException;

import java.util.ArrayList;
import java.util.List;

public final class ShapeUtils {
    private ShapeUtils() {}

    public static ShapeInfo parseShapeInfo(String shapeName, String paramsLine) throws InvalidShapeInputFormatException {
        String[] paramsStr = paramsLine.split(" ");
        List<Double> params = new ArrayList<>();
        try {
            for (String param : paramsStr) {
                params.add(Double.parseDouble(param));
            }
        } catch (NumberFormatException ex) {
            throw new InvalidShapeInputFormatException("Cant convert one of params");
        }
        return new ShapeInfo(shapeName, params);
    }
}
