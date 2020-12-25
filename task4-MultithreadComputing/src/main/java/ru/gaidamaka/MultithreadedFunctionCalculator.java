package ru.gaidamaka;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;


public class MultithreadedFunctionCalculator {
    private static final Logger logger = LoggerFactory.getLogger(MultithreadedFunctionCalculator.class);

    private static final int MAX_THREAD_NUMBER = 100;
    private static final int DEFAULT_START_FUNC_ARGUMENT = 1;

    @NotNull
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
        List<FunctionCalculationTask> tasks = createTaskList();
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
        List<Future<Double>> taskFutures = submitTasksThreadListAndGetFutureList(executor, tasks);
        double result = collectResultOfTasks(taskFutures);
        executor.shutdown();
        return result;
    }

    private double collectResultOfTasks(List<Future<Double>> taskFutures) {
        double result = 0.0;
        for (Future<Double> futureTask : taskFutures) {
            try {
                result += futureTask.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Future task get() exception", e);
            }
        }
        return result;
    }

    private List<Future<Double>> submitTasksThreadListAndGetFutureList(ExecutorService executor, List<FunctionCalculationTask> taskList) {
        return taskList
                .stream()
                .map(executor::submit)
                .collect(Collectors.toList());
    }

    private List<FunctionCalculationTask> createTaskList() {
        List<FunctionCalculationTask> taskList = new ArrayList<>();
        int taskStartArg = startFuncArgument;
        int taskIterationsNumber = 0;
        int iterationsNumber = maxFuncArgument - startFuncArgument + 1;
        for (int threadIndex = 0; threadIndex < threadNumber; threadIndex++) {
            taskStartArg += taskIterationsNumber;
            taskIterationsNumber = countIterationsNumberPerTask(iterationsNumber, threadIndex);
            FunctionCalculationTask task = new FunctionCalculationTask(
                    doubleFunction,
                    taskStartArg,
                    taskIterationsNumber
            );
            taskList.add(task);
        }
        return taskList;
    }

    private int countIterationsNumberPerTask(int iterationsNumber, int threadIndex) {
        return iterationsNumber / threadNumber
                + ((threadIndex < iterationsNumber % threadNumber) ? 1 : 0);
    }
}
