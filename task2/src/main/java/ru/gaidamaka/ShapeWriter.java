package ru.gaidamaka;

import ru.gaidamaka.exception.ShapeOutputException;
import ru.gaidamaka.shape.Shape;

import java.io.*;

public class ShapeWriter implements Closeable {
    private final OutputStreamWriter outputStreamWriter;

    ShapeWriter(OutputStream stream) {
        outputStreamWriter = new OutputStreamWriter(stream);
    }

    public void writeShape(Shape shape, String unit) throws ShapeOutputException {
        try {
            outputStreamWriter.write(makeShapeInfoStr(shape, unit));
            outputStreamWriter.flush();
        } catch (IOException ex) {
            throw new ShapeOutputException("Problem with shape output");
        }
    }

    private String makeShapeInfoStr(Shape shape, String unit) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Тип фигуры: ")
                .append(shape.getType().getShapeName())
                .append(System.lineSeparator())
                .append("Площадь: ")
                .append(shape.getSquare())
                .append(" ")
                .append(UnitType.SQUARE.makeUnitRepresentation(unit))
                .append(System.lineSeparator())
                .append("Периметр: ")
                .append(shape.getPerimeter())
                .append(" ")
                .append(unit)
                .append(System.lineSeparator());

        for (ShapeCharacteristic shapeCharacteristic : shape.getAdditionalInfo()) {
            stringBuilder.append(shapeCharacteristic.getCharacteristicName())
                    .append(": ")
                    .append(shapeCharacteristic.getValue())
                    .append(" ")
                    .append(shapeCharacteristic.getUnitType().makeUnitRepresentation(unit))
                    .append(System.lineSeparator());
        }
        return stringBuilder.toString();

    }


    @Override
    public void close() throws IOException {
        outputStreamWriter.close();
    }
}
