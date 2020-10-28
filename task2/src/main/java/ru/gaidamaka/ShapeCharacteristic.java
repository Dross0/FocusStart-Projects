package ru.gaidamaka;

public class ShapeCharacteristic {
    private final String characteristicName;
    private final double value;
    private final UnitType unitType;


    public ShapeCharacteristic(String characteristicName, double value, UnitType unitType) {
        this.characteristicName = characteristicName;
        this.value = value;
        this.unitType = unitType;
    }

    public double getValue() {
        return value;
    }

    public String getCharacteristicName() {
        return characteristicName;
    }

    public UnitType getUnitType() {
        return unitType;
    }
}
