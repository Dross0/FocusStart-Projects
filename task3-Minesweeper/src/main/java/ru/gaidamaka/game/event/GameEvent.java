package ru.gaidamaka.game.event;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.game.cell.Cell;

import java.util.List;
import java.util.Objects;

public class GameEvent {
    private final List<Cell> updatedCells;
    private final GameEventType eventType;
    private final int score;
    private final int currentBombsNumberWithoutMarkedCells;

    public GameEvent(@NotNull List<Cell> updatedCells,
                     @NotNull GameEventType eventType,
                     int currentBombsNumberWithoutMarkedCells,
                     int score) {
        this.updatedCells = Objects.requireNonNull(updatedCells, "Updated cells list cant be null");
        this.eventType = Objects.requireNonNull(eventType, "Event type cant be null");
        this.score = score;
        this.currentBombsNumberWithoutMarkedCells = currentBombsNumberWithoutMarkedCells;
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

    public int getCurrentBombsNumberWithoutMarkedCells() {
        return currentBombsNumberWithoutMarkedCells;
    }
}
