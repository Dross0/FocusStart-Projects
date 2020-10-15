package ru.gaidamaka;

public class WriterUtils {
    public static String getStringWithNSameChar(char ch, int size){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; ++i){
            stringBuilder.append(ch);
        }
        return stringBuilder.toString();
    }

    public static int getSymbolsCountOfInteger(int number){
        return String.valueOf(number).length();
    }
}
