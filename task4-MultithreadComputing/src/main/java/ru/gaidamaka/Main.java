package ru.gaidamaka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final int ARGUMENTS_NUMBER = 1;
    private static final int MAX_FUNCTION_ARGUMENT_INDEX = 0;
    private static final int THREADS_NUMBER = 10;

    public static void main(String[] args) {
        if (args.length != ARGUMENTS_NUMBER) {
            System.out.println("Wrong arguments number, expected=" + ARGUMENTS_NUMBER + ", actual=" + args.length);
            logger.error("Wrong arguments number, expected={}, actual={}", ARGUMENTS_NUMBER, args.length);
            return;
        }

        OptionalInt maxFuncArgument = parseInteger(args[MAX_FUNCTION_ARGUMENT_INDEX]);

        maxFuncArgument.ifPresentOrElse(
                Main::calculateWithTimeMeasurement,
                () -> {
                    System.out.println("Cant parse integer from {" + args[MAX_FUNCTION_ARGUMENT_INDEX] + "}");
                    logger.error("Cant parse integer from {}", args[MAX_FUNCTION_ARGUMENT_INDEX]);
                }
        );
    }

    private static void calculateWithTimeMeasurement(int maxArgument) {
        try {
            MultithreadedFunctionCalculator calculator = new MultithreadedFunctionCalculator(
                    number -> {
                        double result = number;
                        for (int i = 0; i < 100; i++) {
                            result = Math.sin(result);
                        }
                        return Math.tan(result);
                    },
                    THREADS_NUMBER,
                    maxArgument
            );
            long timeBeforeNS = System.nanoTime();
            double result = calculator.calc();
            long timeAfterNS = System.nanoTime();
            long calcDurationMS = TimeUnit.MILLISECONDS.convert(timeAfterNS - timeBeforeNS, TimeUnit.NANOSECONDS);
            System.out.println("Calculation with "
                    + THREADS_NUMBER + " threads and max function argument = "
                    + maxArgument + " has time = "
                    + calcDurationMS + " ms, result = " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong calculator arguments");
            logger.error("Wrong calculator arguments", e);
        }
    }

    private static OptionalInt parseInteger(String number) {
        try {
            return OptionalInt.of(Integer.parseInt(number));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }
}
