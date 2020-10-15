package ru.gaidamaka;



import ru.gaidamaka.exceptions.WrongTableSizeException;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private final static Logger logger = Logger.getLogger(Main.class.getName());
    private final static String USER_INPUT_MODE_ON_ARG = "-u";

    public static void main(String[] args)  {
        if (args.length != 1){
            logger.log(Level.SEVERE, "Number args should be 1");
            return;
        }
        int tableSize;
        if (args[0].equals(USER_INPUT_MODE_ON_ARG)){
            System.out.println("Введите размер таблицы: ");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()){
                tableSize = scanner.nextInt();
            }
            else{
                logger.log(Level.SEVERE, "Wrong integer input");
                return;
            }
        }
        else{
            try{
                tableSize = Integer.parseInt(args[0], 10);
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE, "Wrong integer input at argument");
                return;
            }
        }
        try{
            MultiplicationTable table = new MultiplicationTable(tableSize);
            table.printTable();
        } catch (WrongTableSizeException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }


}
