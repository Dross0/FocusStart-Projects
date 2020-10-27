package ru.gaidamaka;

import java.util.Formatter;

public enum UnitType {
    // %s - Единицы измерения, которые будут подставлены в форматную строку, генерируемую для каждого UnitType
    LENGTH("%s"),
    ANGLE("°"),
    SQUARE("кв %s");

    public final String format;

    UnitType(String format) {
        this.format = format;
    }

    public String makeUnitRepresentation(String unit){
        Formatter formatter = new Formatter();
        return formatter.format(format, unit).toString();
    }
}
