package ru.gaidamaka;

import ru.gaidamaka.exceptions.ShapeOutputException;

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
        }
        catch (IOException ex){
            throw new ShapeOutputException("Problem with shape output");
        }
    }

    private String makeShapeInfoStr(Shape shape, String unit){
        String resultStr = "Тип фигуры: " + shape.getType().getShapeName() + "\n" +
                "Площадь: " + shape.getSquare() + " " + getUnitRepr(UnitType.SQUARE, unit) + "\n" +
                "Периметр: " + shape.getPerimeter() + " "+ unit + "\n";
        StringBuilder stringBuilder = new StringBuilder();
        for (ShapeCharacteristic shapeCharacteristic: shape.getAdditionalInfo()){
            stringBuilder.append(shapeCharacteristic.getCharacteristicName())
                    .append(": ").append(shapeCharacteristic.getValue()).append(" ")
                    .append(getUnitRepr(shapeCharacteristic.getUnitType(), unit)).append("\n");
        }
        return resultStr + stringBuilder.toString();

    }

    private String getUnitRepr(UnitType type, String unit){
        switch (type){
            case ANGLE:
                return "°";
            case LENGTH:
                return unit;
            case SQUARE:
                return "кв " + unit;
            default:
                return null;
        }
    }

    @Override
    public void close() throws IOException {
        outputStreamWriter.close();
    }
}
