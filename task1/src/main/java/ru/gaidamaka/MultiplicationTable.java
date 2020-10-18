package ru.gaidamaka;

import ru.gaidamaka.exceptions.MultiplicationTableOutOfBoundsException;
import ru.gaidamaka.exceptions.WrongTableSizeException;


public class MultiplicationTable {
    private static final int MAX_TABLE_SIZE = 32;
    private static final int MIN_TABLE_SIZE = 1;

    private final int size;

    public MultiplicationTable(int size) throws WrongTableSizeException {
        if (!validateSize(size)) {
            throw new WrongTableSizeException("Size = " + size + " not from valid interval: ["
                    + MIN_TABLE_SIZE + ", " + MAX_TABLE_SIZE + "]");
        }
        this.size = size;
    }

    private boolean validateSize(int size) {
        return size >= MIN_TABLE_SIZE && size <= MAX_TABLE_SIZE;
    }

    private boolean validateTableCellIndex(int cellIndex) {
        return cellIndex <= size && cellIndex >= 1;
    }

    public int get(int a, int b) {
        if (!(validateTableCellIndex(a) && validateTableCellIndex(b))) {
            throw new MultiplicationTableOutOfBoundsException("Max values = [1, " + size + "]");
        }
        return a * b;
    }

    public int getSize() {
        return size;
    }
}
