package ru.gaidamaka.game;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.GameObservable;
import ru.gaidamaka.GameObserver;
import ru.gaidamaka.game.cell.Cell;
import ru.gaidamaka.game.event.GameEvent;
import ru.gaidamaka.game.event.GameEventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game implements Runnable, GameObservable {
    private static final int DEFAULT_FIELD_WIDTH = 9;
    private static final int DEFAULT_FIELD_HEIGHT = 9;
    private static final int DEFAULT_BOMBS_NUMBER = 10;

    private GameField gameField;

    private final List<Cell> updatedCells;
    private final List<GameObserver> observers;
    private GameStatus gameStatus;
    private int closedCellsNumber;
    private int bombsNumber;
    private int marksNumber;
    private int score;


    public Game(int width, int height, int bombsNumber){
        updatedCells = new ArrayList<>();
        observers = new ArrayList<>();
        reset(width, height, bombsNumber);
    }

    public Game(){
        this(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT, DEFAULT_BOMBS_NUMBER);
    }

    public void reset(int width, int height, int bombsNumber){
        gameField = new GameField(width, height, bombsNumber);
        this.bombsNumber = bombsNumber;
        marksNumber = 0;
        score = 0;
        closedCellsNumber = width * height;
        updatedCells.clear();
        gameStatus = GameStatus.STOPPED;
    }

    private void showCellWithoutNotify(int x, int y){
        Cell cell = gameField.getCell(x, y);
        if (!cell.isHidden() || cell.isMarked()){
            return;
        }
        cell.show();
        closedCellsNumber--;
        updatedCells.add(cell);
        switch (cell.getType()){
            case BOMB:
                lose();
                break;
            case EMPTY:
                List<Cell> nearCells = gameField.getNearCells(x, y);
                nearCells.forEach(nearCell -> showCellWithoutNotify(nearCell.getX(), nearCell.getY()));
                break;
        }
    }

    public void setCurrentScore(int score){
        this.score = score;
    }

    public void showCell(int x, int y){
        if (gameStatus != GameStatus.PLAYING){
            return;
        }
        showCellWithoutNotify(x, y);
        notifyObservers(createGameEvent(GameEventType.MOVE));
        checkWin();
    }

    public void showNeighborsOfOpenCell(int x, int y){
        Cell cell = gameField.getCell(x, y);
        if (gameStatus != GameStatus.PLAYING || cell.isHidden()){
            return;
        }
        List<Cell> nearCells = gameField.getNearCells(x, y);
        int nearFlagsNumber = 0;
        for (Cell nearCell: nearCells){
            if (nearCell.isMarked()){
                nearFlagsNumber++;
            }
        }
        if (nearFlagsNumber != cell.getNearBombNumber()){
            return;
        }
        nearCells.forEach(nearCell -> showCellWithoutNotify(nearCell.getX(), nearCell.getY()));
        notifyObservers(createGameEvent(GameEventType.MOVE));
        checkWin();
    }

    public int getCurrentBombsNumberWithoutMarkedCells(){
        return gameField.getCurrentBombsNumberWithoutMarkedCells();
    }

    public void toggleMarkCell(int x, int y){
        if (gameStatus != GameStatus.PLAYING){
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

    @Override
    public void run() {
        gameStatus = GameStatus.PLAYING;
        for (int row = 0; row < gameField.getHeight(); row++) {
            for (int col = 0; col < gameField.getWidth(); col++) {
                updatedCells.add(gameField.getCell(col, row));
            }

        }
        notifyObservers(createGameEvent(GameEventType.START_GAME));
    }

    private void lose() {
        gameStatus = GameStatus.LOSE;
        notifyObservers(createGameEvent(GameEventType.FINISH_GAME));
    }

    private void checkWin() {
        if (gameStatus == GameStatus.PLAYING && closedCellsNumber == bombsNumber) {
            win();
        }
    }

    private void win() {
        gameStatus = GameStatus.WIN;
        notifyObservers(createGameEvent(GameEventType.FINISH_GAME));
    }

    @Override
    public void addObserver(@NotNull GameObserver gameObserver) {
        Objects.requireNonNull(gameObserver, "Game observer cant be null");
        observers.add(gameObserver);
    }

    @Override
    public void removeObserver(@NotNull GameObserver gameObserver) {
        Objects.requireNonNull(gameObserver, "Game observer cant be null");
        observers.add(gameObserver);
    }

    private GameEvent createGameEvent(GameEventType eventType) {
        final List<Cell> updatedCellsCopy = new ArrayList<>();
        updatedCells.forEach(cell -> updatedCellsCopy.add(new Cell(cell)));
        updatedCells.clear();
        return new GameEvent(updatedCellsCopy, gameStatus, score, eventType);
    }

    @Override
    public void notifyObservers(@NotNull GameEvent event) {
        Objects.requireNonNull(event, "Event cant be null");
        observers.forEach(observer -> observer.update(event));
    }

    public void exit() {
        gameStatus = GameStatus.STOPPED;
    }
}
