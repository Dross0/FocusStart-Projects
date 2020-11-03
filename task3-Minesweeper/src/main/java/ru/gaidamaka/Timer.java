package ru.gaidamaka;

import java.time.Duration;
import java.time.Instant;

public class Timer {
    private int secondsNumber;
    private Thread timer;

    public Timer() {
        this.secondsNumber = 0;
        timer = new Thread(() -> {
            Instant lastTime = Instant.now();
            while (!timer.isInterrupted()){
                Duration duration = Duration.between(lastTime, Instant.now());
                if (duration.abs().getSeconds() >= 1){
                    lastTime = Instant.now();
                    synchronized (this){
                        secondsNumber++;
                    }
                }
            }
        });
    }

    public synchronized int getSeconds()  {
        return secondsNumber;
    }

    public void start(){
        timer.start();
    }

    public void stop(){
        timer.interrupt();
    }
}
