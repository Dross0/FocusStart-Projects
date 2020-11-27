package ru.gaidamaka;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class FunctionCalculationTask implements Callable<Double> {
    @NotNull
    private final Function<Integer, Double> doubleFunction;
    private final int startFuncArgument;
    private final int iterationNumber;

    public FunctionCalculationTask(@NotNull Function<Integer, Double> doubleFunction,
                                   int startFuncArgument,
                                   int iterationNumber) {
        this.doubleFunction = Objects.requireNonNull(doubleFunction, "Function cant be null");
        this.startFuncArgument = startFuncArgument;
        this.iterationNumber = iterationNumber;
    }

    @Override
    public Double call() {
        double sumOfFunctionResults = 0.0;
        int maxArgument = startFuncArgument + iterationNumber;
        for (int funcArgument = startFuncArgument; funcArgument < maxArgument; funcArgument++) {
            sumOfFunctionResults += doubleFunction.apply(funcArgument);
        }
        return sumOfFunctionResults;
    }
}
