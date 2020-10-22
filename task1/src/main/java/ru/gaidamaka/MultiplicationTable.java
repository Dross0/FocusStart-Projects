package ru.gaidamaka;

import ru.gaidamaka.exceptions.MultiplicationTableOutOfBoundsException;
import ru.gaidamaka.exceptions.WrongTableSizeException;


public class MultiplicationTable {
    public static final int MAX_TABLE_SIZE = 32;
    public static final int MIN_TABLE_SIZE = 1;

    private final int size;

    public MultiplicationTable(int size) throws WrongTableSizeException {
        if (!isValidSize(size)) {
            throw new WrongTableSizeException("Size = " + size + " not from valid interval: ["
                    + MIN_TABLE_SIZE + ", " + MAX_TABLE_SIZE + "]");
        }
        this.size = size;
    }

    private boolean isValidSize(int size) {
        return size >= MIN_TABLE_SIZE && size <= MAX_TABLE_SIZE;
    }

    private boolean isValidTableCellIndex(int cellIndex) {
        return cellIndex <= size && cellIndex >= 1;
    }

    public int get(int a, int b) {
        if (isValidTableCellIndex(a) || isValidTableCellIndex(b)) {
            return a * b;
        }
        throw new MultiplicationTableOutOfBoundsException("Max values = [1, " + size + "]");
    }

    public int getSize() {
        return size;
    }
}
