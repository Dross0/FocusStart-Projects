package ru.gaidamaka;


import ru.gaidamaka.exceptions.WrongTableSizeException;

import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String USER_INPUT_MODE_ON_ARG = "-u";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Wrong number of arguments, expected: 1, actual: " + args.length);
            logger.log(Level.SEVERE, "Wrong number of arguments, expected: 1, actual: " + args.length);
            return;
        }
        Optional<Integer> tableSize = (args[0].equals(USER_INPUT_MODE_ON_ARG)) ?
                readTableSizeFromConsole() :
                parseTableSize(args[0]);

        if (tableSize.isEmpty()) {
            System.out.println("Table size cast to integer failure");
            logger.log(Level.SEVERE, "Table size cast to integer failure");
            return;
        }
        try {
            MultiplicationTableWriter tableWriter = new MultiplicationTableWriter(System.out);
            MultiplicationTable table = new MultiplicationTable(tableSize.get());
            tableWriter.writeTable(table);
        } catch (WrongTableSizeException e) {
            String errorMsg = "Size = " + tableSize.get() + " not from valid interval: ["
                    + MultiplicationTable.MIN_TABLE_SIZE + ", " + MultiplicationTable.MAX_TABLE_SIZE + "]";
            System.out.println(errorMsg);
            logger.log(Level.SEVERE, errorMsg, e);
        }
    }

    private static Optional<Integer> parseTableSize(String tableSizeStr) {
        try {
            return Optional.of(Integer.parseInt(tableSizeStr, 10));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    private static Optional<Integer> readTableSizeFromConsole() {
        System.out.println("Input table size: ");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            return Optional.of(scanner.nextInt());
        }
        return Optional.empty();
    }
}
