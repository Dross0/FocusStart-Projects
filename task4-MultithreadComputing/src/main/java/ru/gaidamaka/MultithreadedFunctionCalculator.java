package ru.gaidamaka;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;


public class MultithreadedFunctionCalculator {
    private static final Logger logger = LoggerFactory.getLogger(MultithreadedFunctionCalculator.class);

    private static final int MAX_THREAD_NUMBER = 100;
    private static final int DEFAULT_START_FUNC_ARGUMENT = 1;

    private final Function<Integer, Double> doubleFunction;
    private final int maxFuncArgument;
    private final int threadNumber;
    private final int startFuncArgument;


    /**
     * @param doubleFunction    - function that cant has state or use some outer resources
     * @param threadNumber      - the number of threads into which the function execution will be divided
     * @param maxFuncArgument   - function argument [startFuncArgument, maxFuncArgument]
     * @param startFuncArgument - function argument [startFuncArgument, maxFuncArgument]
     */
    public MultithreadedFunctionCalculator(@NotNull Function<Integer, Double> doubleFunction,
                                           int threadNumber,
                                           int maxFuncArgument,
                                           int startFuncArgument) {
        this.doubleFunction = Objects.requireNonNull(doubleFunction, "Function cant be null");
        validateFuncArguments(maxFuncArgument, startFuncArgument);
        this.maxFuncArgument = maxFuncArgument;
        this.startFuncArgument = startFuncArgument;
        validateThreadNumber(threadNumber);
        this.threadNumber = threadNumber;
    }

    /**
     * @param doubleFunction  - function that cant has state or use some outer resources
     * @param threadNumber    - the number of threads into which the function execution will be divided
     * @param maxFuncArgument - function argument [startFuncArgument, maxFuncArgument], startFunc will have DEFAULT_VALUE = 1
     */
    public MultithreadedFunctionCalculator(@NotNull Function<Integer, Double> doubleFunction, int threadNumber, int maxFuncArgument) {
        this(doubleFunction, threadNumber, maxFuncArgument, DEFAULT_START_FUNC_ARGUMENT);
    }

    private void validateThreadNumber(int threadNumber) {
        if (threadNumber <= 0 || threadNumber > MAX_THREAD_NUMBER) {
            throw new IllegalArgumentException("Thread number not from valid interval = ["
                    + 1 + ", " + MAX_THREAD_NUMBER + "]");
        }
    }

    private void validateFuncArguments(int maxFuncArgument, int startFuncArgument) {
        if (maxFuncArgument <= startFuncArgument) {
            throw new IllegalArgumentException("Max function argument cant be less than start argument = " + startFuncArgument);
        }
    }

    private double oneThreadFunctionCalculation() {
        double sumOfFuncResult = 0.0;
        for (int funcArgument = startFuncArgument; funcArgument <= maxFuncArgument; funcArgument++) {
            sumOfFuncResult += doubleFunction.apply(funcArgument);
        }
        return sumOfFuncResult;
    }

    public double calc() {
        if (threadNumber == 1) {
            return oneThreadFunctionCalculation();
        }
        List<FunctionCalculationTask> taskList = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        int startArg = startFuncArgument;
        int taskIterationsNumber = 0;
        int iterationsNumber = maxFuncArgument - startFuncArgument + 1;
        for (int threadIndex = 0; threadIndex < threadNumber; threadIndex++) {
            startArg += taskIterationsNumber;
            taskIterationsNumber = iterationsNumber / threadNumber
                    + ((threadIndex < iterationsNumber % threadNumber) ? 1 : 0);

            FunctionCalculationTask task = new FunctionCalculationTask(doubleFunction, startArg, taskIterationsNumber);
            taskList.add(task);
            threads.add(new Thread(task));
        }
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                logger.error("Join interrupted", e);
            }
        });
        double result = 0.0;
        for (FunctionCalculationTask task : taskList) {
            result += task.getResult();
        }
        return result;
    }
}
