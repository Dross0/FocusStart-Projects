package ru.gaidamaka;

import javafx.util.Pair;
import ru.gaidamaka.exceptions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final String CONSOLE_MODE_ARG = "-c";
    private static final String FILE_MODE_ARG = "-f";

    private static final String UNIT = "см";

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Number args should be 2 or 3");
            logger.log(Level.SEVERE, "Wrong arguments number, actual = " + args.length +", expected = 2 or 3");
            return;
        }

        Pair<String, ArrayList<Double>> shapeInfo;
        try (ShapeReader shapeReader = new ShapeReader(Files.newInputStream(Paths.get(args[0])))){
            shapeInfo = shapeReader.readShape();
        } catch (IOException e) {
            System.out.println();
            logger.log(Level.SEVERE, "Cant open input file = {" + args[0] + "}");
            return;
        } catch (InvalidShapeInputFormat invalidShapeInputFormat) {
            System.out.println("Input file contain shape with wrong format");
            logger.log(Level.SEVERE, "Wrong shape input format");
            return;
        }

        try {
            Shape shape = ShapeFactory.getSquare(shapeInfo.getKey(), shapeInfo.getValue());
            ShapeWriter shapeWriter;
            switch (args[1]){
                case CONSOLE_MODE_ARG:
                    if (args.length == 3){
                        logger.log(Level.WARNING, "Output file will be ignored because console mode on");
                    }
                    shapeWriter = new ShapeWriter(System.out);
                    shapeWriter.writeShape(shape, UNIT);
                    break;
                case FILE_MODE_ARG:
                    if (args.length != 3){
                        System.out.println("No file name at 3rd argument");
                        logger.log(Level.SEVERE, "No file name at 3rd argument");
                        return;
                    }
                    shapeWriter = new ShapeWriter(Files.newOutputStream(Paths.get(args[2])));
                    shapeWriter.writeShape(shape, UNIT);
                    shapeWriter.close();
                    break;
                default:
                    System.out.println("Wrong input mode arg = " + args[0]);
                    logger.log(Level.SEVERE, "Wrong input mode arg = " + args[0]);
            }
        } catch (InvalidShapeTypeName | InvalidShapeArgument |
                ShapeFactoryUnsupportedType | ShapeFactoryParamsException | ShapeOutputException exception) {
            System.out.println(exception.getMessage());
            logger.log(Level.SEVERE, exception.getMessage());
        }
        catch (IOException ex){
            System.out.println("Cant open output file");
            logger.log(Level.SEVERE, "Cant open output file");
        }
    }
}
