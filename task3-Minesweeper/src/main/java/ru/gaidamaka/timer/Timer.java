package ru.gaidamaka.timer;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class Timer implements TimerObservable {
    public static final int TICK_PERIOD_MS = 1000;

    private final List<TimerObserver> observers;
    private final Thread timerThread;
    private volatile boolean isPaused;

    public Timer() {
        observers = new CopyOnWriteArrayList<>();
        isPaused = true;
        timerThread = new Thread(getTimerRunnable());
    }

    private Runnable getTimerRunnable() {
        return () -> {
            long lastTimeNs = System.nanoTime();
            while (!timerThread.isInterrupted()) {
                long currentTimeNs = System.nanoTime();
                long betweenTimeMS = TimeUnit.NANOSECONDS.toMillis(currentTimeNs - lastTimeNs);
                if (betweenTimeMS >= TICK_PERIOD_MS) {
                    lastTimeNs = currentTimeNs;
                    if (!isPaused) {
                        notifyObservers();
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

    @Override
    public void addObserver(@NotNull TimerObserver timerObserver) {
        Objects.requireNonNull(timerObserver, "Timer observer cant be null");
        observers.add(timerObserver);
    }

    @Override
    public void removeObserver(@NotNull TimerObserver timerObserver) {
        Objects.requireNonNull(timerObserver, "Timer observer cant be null");
        observers.remove(timerObserver);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(TimerObserver::updateTimer);
    }
}
