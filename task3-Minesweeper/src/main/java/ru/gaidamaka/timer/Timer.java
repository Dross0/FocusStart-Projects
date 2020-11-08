package ru.gaidamaka.timer;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Timer implements TimerObservable {
    private final List<TimerObserver> observers;
    private int secondsNumber;
    private Thread timer;

    public Timer() {
        this.secondsNumber = 0;
        observers = new ArrayList<>();
        timer = new Thread(() -> {
            Instant lastTime = Instant.now();
            while (!timer.isInterrupted()){
                Duration duration = Duration.between(lastTime, Instant.now());
                if (duration.abs().getSeconds() >= 1){
                    lastTime = Instant.now();
                    synchronized (this){
                        secondsNumber++;
                    }
                    notifyObservers();
                }
            }
        });
    }

    public synchronized int getSeconds()  {
        return secondsNumber;
    }

    public void start(){
        if (!timer.isAlive()){
            timer.start();
        }

    }

    public void stop(){
        timer.interrupt();
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
