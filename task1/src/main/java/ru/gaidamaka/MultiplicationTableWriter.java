package ru.gaidamaka;


import java.io.Closeable;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Formatter;

public class MultiplicationTableWriter implements Closeable {
    private final PrintWriter writer;

    public MultiplicationTableWriter(OutputStream outputSteam) {
        this.writer = new PrintWriter(outputSteam);
    }

    public void writeTable(MultiplicationTable table) {
        int tableSize = table.getSize();
        final int fieldSize = Utils.getSymbolsCount(tableSize * tableSize);
        final int firstColumnFieldSize = Utils.getSymbolsCount(tableSize);
        for (int row = 0; row <= tableSize; row++) {
            if (row == 0) {
                printHeader(table, fieldSize, firstColumnFieldSize);
            } else {
                printRow(table, row, fieldSize, firstColumnFieldSize);
            }
            writer.println();
            printRowsSeparator(tableSize, fieldSize, firstColumnFieldSize);
            writer.println();
        }
        writer.flush();
    }

    private void printRowsSeparator(int tableSize, int fieldSize, int firstColumnFieldSize) {
        writer.print("-".repeat(firstColumnFieldSize) + "+");
        String rowsSeparator = "-".repeat(fieldSize);
        for (int i = 1; i <= tableSize; i++) {
            if (i == tableSize) {
                writer.print(rowsSeparator);
            } else {
                writer.print(rowsSeparator + "+");
            }
        }
    }


    private void printRow(MultiplicationTable table, int factor, int fieldSize, int firstColumnFieldSize) {
        Formatter formatter = new Formatter();
        printRowWithFirstElement(table, factor, fieldSize, formatter.format("%" + firstColumnFieldSize + "d|", factor).toString());
    }

    private void printHeader(MultiplicationTable table, int fieldSize, int firstColumnFieldSize) {
        printRowWithFirstElement(table, 1, fieldSize, " ".repeat(firstColumnFieldSize) + "|");
    }


    private void printRowWithFirstElement(MultiplicationTable table, int factor, int fieldSize, String firstElement) {
        String format = "%" + fieldSize + "d|";
        String lastElemFormat = "%" + fieldSize + "d";
        writer.print(firstElement);
        for (int i = 1; i < table.getSize(); i++) {
            writer.printf(format, table.get(factor, i));
        }
        writer.printf(lastElemFormat, table.get(factor, table.getSize()));
    }

    @Override
    public void close() {
        writer.close();
    }
}
