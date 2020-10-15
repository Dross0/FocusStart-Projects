package ru.gaidamaka;

import ru.gaidamaka.exceptions.WrongTableSizeException;

import java.util.Formatter;

public class MultiplicationTable {
    private final static int MAX_TABLE_SIZE = 32;
    private final static int MIN_TABLE_SIZE = 1;

    private final int size;

    public MultiplicationTable(int size) throws WrongTableSizeException {
        if (!validateSize(size)){
            throw new WrongTableSizeException("Size = " + size + " not from valid interval: ["
                    + MIN_TABLE_SIZE +", " + MAX_TABLE_SIZE + "]");
        }
        this.size = size;
    }

    private boolean validateSize(int size){
        return size >= MIN_TABLE_SIZE && size <= MAX_TABLE_SIZE;
    }

    public void printTable(){
        final int fieldSize = WriterUtils.getSymbolsCountOfInteger(size * size);
        final int factorFieldSize = WriterUtils.getSymbolsCountOfInteger(size);
        for (int row = 0; row <= size; ++row){
            if (row == 0){
                printHeader(fieldSize, factorFieldSize);
            }
            else{
                printRow(row, fieldSize, factorFieldSize);
            }
            System.out.println();
            printRowsSeparator(fieldSize, factorFieldSize);
            System.out.println();
        }
    }

    private void printRowsSeparator(int fieldSize, int factorFieldSize){
        System.out.print(WriterUtils.getStringWithNSameChar('-', factorFieldSize) + "+");
        String rowsSeparator = WriterUtils.getStringWithNSameChar('-', fieldSize);
        for (int i = 1; i <= size; ++i){
            if (i == size){
                System.out.print(rowsSeparator);
            }
            else{
                System.out.print(rowsSeparator + "+");
            }
        }
    }


    private void printRow(int factor, int fieldSize, int factorFieldSize){
        Formatter formatter = new Formatter();
        printRowWithFirstElement(factor, fieldSize, formatter.format("%" + factorFieldSize + "d|", factor).toString());
    }

    private void printHeader(int fieldSize, int factorFieldSize){
        printRowWithFirstElement(1, fieldSize, WriterUtils.getStringWithNSameChar(' ', factorFieldSize) + "|");
    }



    private void printRowWithFirstElement(int factor, int fieldSize, String firstElement) {
        String format = "%" + fieldSize + "d|";
        String lastElemFormat = "%" + fieldSize + "d";
        System.out.print(firstElement);
        for (int i = 1; i < size; ++i){
            System.out.printf(format, factor * i);
        }
        System.out.printf(lastElemFormat, factor * size);
    }
}
