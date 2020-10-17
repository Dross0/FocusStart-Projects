package ru.gaidamaka;


import java.util.ArrayList;

public interface Shape {
    ShapeType getType();
    double getSquare();
    double getPerimeter();
    ArrayList<ShapeCharacteristic> getAdditionalInfo();
}
