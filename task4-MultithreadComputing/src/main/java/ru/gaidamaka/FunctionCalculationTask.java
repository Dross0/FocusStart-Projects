package ru.gaidamaka;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public class FunctionCalculationTask implements Runnable {
    private final Function<Integer, Double> doubleFunction;
    private final int startFuncArgument;
    private final int iterationNumber;
    private double sumOfFunctionResults;

    public FunctionCalculationTask(@NotNull Function<Integer, Double> doubleFunction,
                                   int startFuncArgument,
                                   int iterationNumber) {
        this.doubleFunction = Objects.requireNonNull(doubleFunction, "Function cant be null");
        this.startFuncArgument = startFuncArgument;
        this.iterationNumber = iterationNumber;
        this.sumOfFunctionResults = 0.0;
    }

    public synchronized double getResult() {
        return sumOfFunctionResults;
    }

    @Override
    public void run() {
        synchronized (this) {
            int maxArgument = startFuncArgument + iterationNumber;
            for (int funcArgument = startFuncArgument; funcArgument < maxArgument; funcArgument++) {
                sumOfFunctionResults += doubleFunction.apply(funcArgument);
            }
        }
    }
}
