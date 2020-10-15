package ru.gaidamaka;

import ru.gaidamaka.exceptions.WrongTableSizeException;

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

    public void printTable(){
    }

    private boolean validateSize(int size){
        return size >= MIN_TABLE_SIZE && size <= MAX_TABLE_SIZE;
    }
}
