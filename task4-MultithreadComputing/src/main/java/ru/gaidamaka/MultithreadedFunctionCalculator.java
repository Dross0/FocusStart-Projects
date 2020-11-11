package ru.gaidamaka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class MultithreadedFunctionCalculator {
    private static final Logger logger = LoggerFactory.getLogger(MultithreadedFunctionCalculator.class);

    private static final int MAX_THREAD_NUMBER = 100;
    private static final int DEFAULT_START_FUNC_ARGUMENT = 1;

    private final int maxFuncArgument;
    private final int threadNumber;
    private final int startFuncArgument;

    public MultithreadedFunctionCalculator(int threadNumber, int maxFuncArgument, int startFuncArgument) {
        validateFuncArguments(maxFuncArgument, startFuncArgument);
        this.maxFuncArgument = maxFuncArgument;
        this.startFuncArgument = startFuncArgument;
        validateThreadNumber(threadNumber);
        this.threadNumber = threadNumber;
    }

    public MultithreadedFunctionCalculator(int threadNumber, int maxFuncArgument) {
        this(threadNumber, maxFuncArgument, DEFAULT_START_FUNC_ARGUMENT);
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


    public double calc() {
        List<FunctionCalculationTask> taskList = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        int startArg = startFuncArgument;
        int taskIterationsNumber = 0;
        int iterationsNumber = maxFuncArgument - startFuncArgument + 1;
        for (int threadIndex = 0; threadIndex < threadNumber; threadIndex++) {
            startArg += taskIterationsNumber;
            taskIterationsNumber = iterationsNumber / threadNumber
                    + ((threadIndex < iterationsNumber % threadNumber) ? 1 : 0);

            FunctionCalculationTask task = new FunctionCalculationTask(startArg, taskIterationsNumber);
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
