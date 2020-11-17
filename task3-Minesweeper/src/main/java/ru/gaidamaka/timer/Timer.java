package ru.gaidamaka.timer;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Timer implements TimerObservable {

    private final List<TimerObserver> observers;
    private final AtomicInteger secondsNumber;
    private Thread timerThread;
    private final AtomicBoolean isPaused;

    public Timer() {
        this.secondsNumber = new AtomicInteger(0);
        observers = new ArrayList<>();
        isPaused = new AtomicBoolean(true);
        timerThread = new Thread(() -> {
            Instant lastTime = Instant.now();
            while (!timerThread.isInterrupted()) {
                Duration duration = Duration.between(lastTime, Instant.now());
                if (duration.abs().getSeconds() >= 1) {
                    lastTime = Instant.now();
                    if (!isPaused.get()) {
                        secondsNumber.incrementAndGet();
                        notifyObservers();
                    }
                }
            }
        });
    }

    public int getSeconds() {
        return secondsNumber.get();
    }

    public void start() {
        isPaused.getAndSet(false);
        if (!timerThread.isAlive()) {
            timerThread.start();
        }
    }

    public boolean isPaused() {
        return isPaused.get();
    }

    public void pause() {
        isPaused.getAndSet(true);
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
