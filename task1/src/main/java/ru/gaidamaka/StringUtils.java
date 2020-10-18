package ru.gaidamaka;

public final class StringUtils {
    private StringUtils() {}

    public static int getSymbolsCountOfInteger(int number) {
        return String.valueOf(number).length();
    }
}
