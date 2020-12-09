package ru.gaidamaka.timer;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Timer {
    public static final int TICK_PERIOD_MS = 1000;

    private final List<Consumer<Integer>> tickCallbacks;
    private final Thread timerThread;
    private volatile boolean isPaused;

    public Timer() {
        tickCallbacks = new CopyOnWriteArrayList<>();
        isPaused = true;
        timerThread = new Thread(getTimerRunnable());
    }

    private Runnable getTimerRunnable() {
        return () -> {
            long lastTimeNs = System.nanoTime();
            int ticksAfterPause = 0;
            while (!timerThread.isInterrupted()) {
                long currentTimeNs = System.nanoTime();
                long betweenTimeMS = TimeUnit.NANOSECONDS.toMillis(currentTimeNs - lastTimeNs);
                if (betweenTimeMS >= TICK_PERIOD_MS) {
                    lastTimeNs = currentTimeNs;
                    ticksAfterPause++;
                    if (!isPaused) {
                        acceptAllCallbacks(ticksAfterPause);
                    } else {
                        ticksAfterPause = 0;
                    }
                }
            }
        };
    }

    public void start() {
        isPaused = false;
        if (!timerThread.isAlive()) {
            timerThread.start();
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void pause() {
        isPaused = true;
    }

    public void stop() {
        pause();
        timerThread.interrupt();
    }

    public void addTickCallback(@NotNull Consumer<Integer> tickCallback) {
        Objects.requireNonNull(tickCallback, "Tick callback cant be null");
        tickCallbacks.add(tickCallback);
    }


    public void removeObserver(@NotNull Consumer<Integer> tickCallback) {
        Objects.requireNonNull(tickCallback, "Tick callback cant be null");
        tickCallbacks.remove(tickCallback);
    }


    private void acceptAllCallbacks(int ticks) {
        tickCallbacks.forEach(tickConsumer -> tickConsumer.accept(ticks));
    }
}
