package ru.gaidamaka;

import ru.gaidamaka.exception.InvalidShapeInputFormatException;

import java.io.Closeable;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShapeReader implements Closeable {
    private final Scanner scanner;

    ShapeReader(InputStream inputStream) {
        scanner = new Scanner(inputStream);
    }

    /**
     * @return Массив строк размера 2 = [имя фигуры, строка с параметрами фигуры]
     */
    List<String> readShapeParameters() throws InvalidShapeInputFormatException {
        String shapeName;
        if (!scanner.hasNextLine()) {
            throw new InvalidShapeInputFormatException("Cant read shape name");
        }
        shapeName = scanner.nextLine();
        if (!scanner.hasNextLine()) {
            throw new InvalidShapeInputFormatException("Cant read params");
        }
        String paramsLine = scanner.nextLine();

        List<String> shapeParams = new ArrayList<>();
        shapeParams.add(shapeName);
        shapeParams.add(paramsLine);
        return shapeParams;
    }

    @Override
    public void close() {
        scanner.close();
    }
}
