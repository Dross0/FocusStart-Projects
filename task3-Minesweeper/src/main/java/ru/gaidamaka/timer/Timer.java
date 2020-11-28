package ru.gaidamaka.timer;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Timer implements TimerObservable {

    private final List<TimerObserver> observers;
    private Thread timerThread;
    private volatile boolean isPaused;

    public Timer() {
        observers = new ArrayList<>();
        isPaused = true;
        timerThread = new Thread(() -> {
            Instant lastTime = Instant.now();
            while (!timerThread.isInterrupted()) {
                Duration duration = Duration.between(lastTime, Instant.now());
                if (duration.abs().getSeconds() >= 1) {
                    lastTime = Instant.now();
                    if (!isPaused) {
                        notifyObservers();
                    }
                }
            }
        });
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
