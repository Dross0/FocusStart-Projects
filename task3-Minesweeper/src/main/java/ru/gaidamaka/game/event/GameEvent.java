package ru.gaidamaka.game.event;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.game.GameStatus;
import ru.gaidamaka.game.cell.Cell;

import java.util.List;
import java.util.Objects;

public class GameEvent {
    private final List<Cell> updatedCells;
    private final GameStatus currentStatus;
    private final GameEventType eventType;
    private final int score;
    private boolean newHighScore;

    public GameEvent(@NotNull List<Cell> updatedCells,
                     @NotNull GameStatus currentStatus,
                     @NotNull GameEventType eventType,
                     int score) {
        this.updatedCells = Objects.requireNonNull(updatedCells, "Updated cells list cant be null");
        this.currentStatus = Objects.requireNonNull(currentStatus, "Game status cant be null");
        this.eventType = Objects.requireNonNull(eventType, "Event type cant be null");
        this.score = score;
        this.newHighScore = false;
    }

    public void newHighScore() {
        newHighScore = true;
    }

    public boolean isNewHighScore() {
        return newHighScore;
    }

    @NotNull
    public GameEventType getEventType() {
        return eventType;
    }

    public int getScore() {
        return score;
    }

    @NotNull
    public List<Cell> getUpdatedCells() {
        return updatedCells;
    }

    @NotNull
    public GameStatus getCurrentGameStatus() {
        return currentStatus;
    }
}
