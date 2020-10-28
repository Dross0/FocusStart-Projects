package ru.gaidamaka.shape;


import ru.gaidamaka.ShapeCharacteristic;
import ru.gaidamaka.ShapeType;

import java.util.List;

public interface Shape {
    ShapeType getType();

    double getSquare();

    double getPerimeter();

    List<ShapeCharacteristic> getAdditionalInfo();
}
