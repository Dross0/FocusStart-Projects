package ru.gaidamaka.game;

import java.util.ArrayList;
import java.util.List;

public class Game implements Runnable{
    private static final int DEFAULT_FIELD_WIDTH = 9;
    private static final int DEFAULT_FIELD_HEIGHT = 9;
    private static final int DEFAULT_BOMBS_NUMBER = 10;

    private final GameField gameField;

    private final List<Cell> updatedCells;
    private GameStatus gameStatus;
    private final int bombsNumber;
    private int marksNumber;

    public Game(int width, int height, int bombsNumber){
        gameField = new GameField(width, height, bombsNumber);
        this.bombsNumber = bombsNumber;
        marksNumber = 0;
        updatedCells = new ArrayList<>();
        gameStatus = GameStatus.STOPPED;
    }

    public Game(){
        this(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT, DEFAULT_BOMBS_NUMBER);
    }

    public List<Cell> getUpdatedCells() {
        return updatedCells;
    }

    public void showCell(int x, int y){
        Cell cell = gameField.getCell(x, y);
        if (!cell.isHidden()){
            return;
        }
        cell.show();
        updatedCells.add(cell);
        switch (cell.getType()){
            case BOMB:
                lose();
                break;
            case EMPTY:
                List<Cell> nearCells = gameField.getNearCells(x, y);
                for (Cell nearCell: nearCells){
                    showCell(nearCell.getX(), nearCell.getY());
                }
                break;
        }
    }

    public void toggleMarkCell(int x, int y){
        Cell cell = gameField.getCell(x, y);
        if (cell.isMarked()){
            cell.unmark();
            marksNumber--;
            updatedCells.add(cell);
        }
        else{
            markCell(x, y);
        }
    }

    public void markCell(int x, int y){
        if (marksNumber + 1 > bombsNumber){
            return;
        }
        Cell cell = gameField.getCell(x, y);
        cell.mark();
        marksNumber++;
        updatedCells.add(cell);
    }

    @Override
    public void run() {
        gameStatus = GameStatus.PLAYING;
    }

    private void lose(){
        gameStatus = GameStatus.LOSE;
    }

    private void win(){
        gameStatus = GameStatus.WIN;
    }
}
