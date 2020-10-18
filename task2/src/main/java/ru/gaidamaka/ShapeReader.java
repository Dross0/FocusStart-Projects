package ru.gaidamaka;

import javafx.util.Pair;
import ru.gaidamaka.exceptions.InvalidShapeInputFormat;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ShapeReader {
    private final Scanner scanner;

    ShapeReader(InputStream inputStream) {
        scanner = new Scanner(inputStream);
    }

    /**
     * @return Пару = {Тип фигуры, вещественные агрументы}
     */
    Pair<String, ArrayList<Double>> readShape() throws InvalidShapeInputFormat {
        String shapeName;
        if (!scanner.hasNextLine()) {
            throw new InvalidShapeInputFormat("Cant read shape name");
        }
        shapeName = scanner.nextLine();
        if (!scanner.hasNextLine()) {
            throw new InvalidShapeInputFormat("Cant read params");
        }
        String paramsLine = scanner.nextLine();
        String[] paramsStr = paramsLine.split(" ");
        ArrayList<Double> params = new ArrayList<>();
        try {
            for (String param : paramsStr) {
                params.add(Double.parseDouble(param));
            }
        } catch (NumberFormatException ex) {
            throw new InvalidShapeInputFormat("Cant convert one of params");
        }
        return new Pair<>(shapeName, params);
    }
}
