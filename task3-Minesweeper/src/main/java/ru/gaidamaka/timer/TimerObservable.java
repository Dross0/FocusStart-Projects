package ru.gaidamaka.timer;

public interface TimerObservable {
    void addObserver(TimerObserver timerObserver);

    void removeObserver(TimerObserver timerObserver);

    void notifyObservers();
}
