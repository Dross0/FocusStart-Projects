package ru.gaidamaka;

public class FunctionCalculationTask implements Runnable {
    private final int startFuncArgument;
    private final int iterationNumber;
    private double sumOfFunctionResults;

    public FunctionCalculationTask(int startFuncArgument, int iterationNumber) {
        this.startFuncArgument = startFuncArgument;
        this.iterationNumber = iterationNumber;
        this.sumOfFunctionResults = 0.0;
    }

    public synchronized double getResult() {
        return sumOfFunctionResults;
    }

    private double function(int x) {
        double result = x;
        for (int i = 0; i < 100; i++) {
            result = Math.sin(result);
        }
        return Math.tan(result);
    }

    @Override
    public void run() {
        synchronized (this) {
            int maxArgument = startFuncArgument + iterationNumber;
            for (int funcArgument = startFuncArgument; funcArgument < maxArgument; funcArgument++) {
                sumOfFunctionResults += function(funcArgument);
            }
        }
    }
}
