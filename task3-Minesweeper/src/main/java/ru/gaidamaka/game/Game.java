package ru.gaidamaka.game;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.GameObservable;
import ru.gaidamaka.GameObserver;
import ru.gaidamaka.game.cell.Cell;
import ru.gaidamaka.game.cell.CellType;
import ru.gaidamaka.game.event.GameEvent;
import ru.gaidamaka.game.event.GameEventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game implements GameObservable {
    private GameField gameField;

    private final List<Cell> updatedCells;
    private final List<GameObserver> observers;
    private int closedCellsNumber;
    private int bombsNumber;
    private int marksNumber;
    private int score;
    private boolean isGameRunning;
    private boolean wasLose;


    public Game(int width, int height, int bombsNumber) {
        updatedCells = new ArrayList<>();
        observers = new ArrayList<>();
        reset(width, height, bombsNumber);
    }

    public void reset(int width, int height, int bombsNumber) {
        gameField = new GameField(width, height, bombsNumber);
        this.bombsNumber = bombsNumber;
        marksNumber = 0;
        score = 0;
        closedCellsNumber = width * height;
        updatedCells.clear();
        isGameRunning = false;
        wasLose = false;
    }

    private void showCellWithoutNotify(int x, int y) {
        Cell cell = gameField.getCell(x, y);
        if (!cell.isHidden() || cell.isMarked()) {
            return;
        }
        cell.show();
        closedCellsNumber--;
        updatedCells.add(cell);
        CellType cellType = cell.getType();
        if (cellType == CellType.BOMB) {
            wasLose = true;
        } else if (cellType == CellType.EMPTY) {
            List<Cell> nearCells = gameField.getNearCells(x, y);
            nearCells.forEach(nearCell -> showCellWithoutNotify(nearCell.getX(), nearCell.getY()));
        }
    }

    public void setCurrentScore(int score){
        this.score = score;
    }

    public void showCell(int x, int y) {
        if (!isGameRunning) {
            return;
        }
        showCellWithoutNotify(x, y);
        if (wasLose) {
            lose();
            return;
        }
        notifyObservers(createGameEvent(GameEventType.MOVE));
        checkWin();
    }

    public void showNeighborsOfOpenCell(int x, int y){
        Cell cell = gameField.getCell(x, y);
        if (!isGameRunning || cell.isHidden()) {
            return;
        }
        List<Cell> nearCells = gameField.getNearCells(x, y);
        int nearFlagsNumber = 0;
        for (Cell nearCell : nearCells) {
            if (nearCell.isMarked()) {
                nearFlagsNumber++;
            }
        }
        if (nearFlagsNumber != cell.getNearBombNumber()) {
            return;
        }
        nearCells.forEach(nearCell -> showCellWithoutNotify(nearCell.getX(), nearCell.getY()));
        if (wasLose) {
            lose();
            return;
        }
        notifyObservers(createGameEvent(GameEventType.MOVE));
        checkWin();
    }

    public void toggleMarkCell(int x, int y){
        if (!isGameRunning) {
            return;
        }
        Cell cell = gameField.getCell(x, y);
        if (cell.isMarked()){
            cell.unmark();
            marksNumber--;
            updatedCells.add(cell);
        }
        else{
            markCell(cell);
        }
        notifyObservers(createGameEvent(GameEventType.MOVE));
    }

    private void markCell(Cell cell){
        if (!cell.isHidden()){  //marksNumber + 1 > bombsNumber - для ограничения количества флагов
            return;
        }
        cell.mark();
        marksNumber++;
        updatedCells.add(cell);
    }

    public void run() {
        isGameRunning = true;
        for (int row = 0; row < gameField.getHeight(); row++) {
            for (int col = 0; col < gameField.getWidth(); col++) {
                updatedCells.add(gameField.getCell(col, row));
            }

        }
        notifyObservers(createGameEvent(GameEventType.START_GAME));
    }

    private void lose() {
        isGameRunning = false;
        notifyObservers(createGameEvent(GameEventType.LOSE));
    }

    private void checkWin() {
        if (isGameRunning && closedCellsNumber == bombsNumber) {
            win();
        }
    }

    private void win() {
        isGameRunning = false;
        notifyObservers(createGameEvent(GameEventType.WIN));
    }

    @Override
    public void addObserver(@NotNull GameObserver gameObserver) {
        Objects.requireNonNull(gameObserver, "Game observer cant be null");
        observers.add(gameObserver);
    }

    @Override
    public void removeObserver(@NotNull GameObserver gameObserver) {
        Objects.requireNonNull(gameObserver, "Game observer cant be null");
        observers.remove(gameObserver);
    }

    private GameEvent createGameEvent(GameEventType eventType) {
        final List<Cell> updatedCellsCopy = new ArrayList<>();
        updatedCells.forEach(
                cell -> updatedCellsCopy.add(new Cell(cell))
        );
        updatedCells.clear();
        return new GameEvent(
                updatedCellsCopy,
                eventType,
                gameField.getCurrentBombsNumberWithoutMarkedCells(),
                score
        );
    }

    @Override
    public void notifyObservers(@NotNull GameEvent event) {
        Objects.requireNonNull(event, "Event cant be null");
        observers.forEach(observer -> observer.update(event));
    }

    public void exit() {
        isGameRunning = false;
    }
}
