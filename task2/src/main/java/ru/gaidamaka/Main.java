package ru.gaidamaka;

import ru.gaidamaka.exception.*;
import ru.gaidamaka.shape.Shape;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final String CONSOLE_MODE_ARG = "-c";
    private static final String FILE_MODE_ARG = "-f";
    private static final int CONSOLE_MODE_ARGS_NUMBER = 2;
    private static final int FILE_MODE_ARGS_NUMBER = 3;

    private static final int INPUT_FILE_NAME_ARG_INDEX = 0;
    private static final int INPUT_MODE_ARG_INDEX = 1;
    private static final int OUTPUT_FILE_NAME_ARG_INDEX = 2;

    private static final String UNIT = "см";

    public static void main(String[] args) {
        if (args.length < CONSOLE_MODE_ARGS_NUMBER || args.length > FILE_MODE_ARGS_NUMBER) {
            System.out.println("Number args should be " + CONSOLE_MODE_ARGS_NUMBER + " or " + FILE_MODE_ARGS_NUMBER);
            logger.log(Level.SEVERE, "Wrong arguments number, actual = " + args.length + ", expected = " +
                    CONSOLE_MODE_ARGS_NUMBER + " or " + FILE_MODE_ARGS_NUMBER);
            return;
        }
        String outputFileName = (args.length == FILE_MODE_ARGS_NUMBER)
                ? args[OUTPUT_FILE_NAME_ARG_INDEX]
                : null;
        readAndParseShapeInfo(args[INPUT_FILE_NAME_ARG_INDEX])
                .ifPresent(info -> createAndWriteShape(info, args[INPUT_MODE_ARG_INDEX], outputFileName));
    }

    private static void createAndWriteShape(ShapeInfo shapeInfo, String inputMode, String outputFileName) {
        try {
            Shape shape = ShapeFactory.createShape(shapeInfo.getName(), shapeInfo.getArgs());
            writeShape(shape, inputMode, outputFileName);
        } catch (ShapeException exception) {
            System.out.println(exception.getMessage());
            logger.log(Level.SEVERE, exception.getMessage());
        } catch (IOException ex) {
            System.out.println("Cant open output file");
            logger.log(Level.SEVERE, "Cant open output file");
        }
    }

    private static void writeShape(Shape shape, String inputMode, String outputFileName) throws IOException, ShapeOutputException {
        switch (inputMode) {
            case CONSOLE_MODE_ARG:
                if (outputFileName != null) {
                    logger.log(Level.WARNING, "Output file will be ignored because console mode on");
                }
                ShapeWriter consoleShapeWriter = new ShapeWriter(System.out);
                consoleShapeWriter.writeShape(shape, UNIT);
                break;
            case FILE_MODE_ARG:
                if (outputFileName == null) {
                    System.out.println("No file name at 3rd argument");
                    logger.log(Level.SEVERE, "No file name at 3rd argument");
                    return;
                }
                try (ShapeWriter fileShapeWriter = new ShapeWriter(Files.newOutputStream(Paths.get(outputFileName)))) {
                    fileShapeWriter.writeShape(shape, UNIT);
                }
                break;
            default:
                System.out.println("Wrong input mode arg = " + inputMode);
                logger.log(Level.SEVERE, "Wrong input mode arg = " + inputMode);
        }
    }

    private static Optional<ShapeInfo> readAndParseShapeInfo(String fileName) {
        try (ShapeReader shapeReader = new ShapeReader(Files.newInputStream(Paths.get(fileName)))) {
            List<String> shapeInfoLines = shapeReader.readShapeParameters();
            return Optional.of(ShapeUtils.parseShapeInfo(shapeInfoLines.get(0), shapeInfoLines.get(1)));
        } catch (IOException e) {
            System.out.println("Cant open input file = {" + fileName + "}");
            logger.log(Level.SEVERE, "Cant open input file = {" + fileName + "}");
        } catch (InvalidShapeInputFormatException invalidShapeInputFormatException) {
            System.out.println("Input file contain shape with wrong format");
            logger.log(Level.SEVERE, "Wrong shape input format");
        }
        return Optional.empty();
    }
}
