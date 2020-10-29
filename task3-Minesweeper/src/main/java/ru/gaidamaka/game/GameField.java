package ru.gaidamaka.game;

import ru.gaidamaka.exception.GameFieldException;

import java.util.*;

public class GameField {
    private static final int MAX_FIELD_WIDTH = 32;
    private static final int MAX_FIELD_HEIGHT = 32;

    private final Cell[][] field;
    private final int width;
    private final int height;
    private final List<Cell> bombs;

    public GameField(int width, int height, int bombsNumber){
        validateFieldParams(width, height, bombsNumber);
        this.width = width;
        this.height = height;
        field = new Cell[height][width];
        fillField();
        bombs = new ArrayList<>(bombsNumber);
        generateBombs(bombsNumber);
        countNearBombsForAllCells();
    }

    public int getCurrentBombsNumberWithoutMarkedCells(){
        return bombs.size() - countMarkedCells();
    }

    private int countMarkedCells(){
        int markedCellsNumber = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (field[row][col].isMarked()){
                    markedCellsNumber++;
                }
            }
        }
        return markedCellsNumber;
    }

    public Cell getCell(int x, int y){
        validateCoordinates(x, y);
        return field[y][x];
    }

    public List<Cell> getNearCells(int x, int y){
        List<Cell> nearCells = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0){
                    continue;
                }
                try {
                    validateCoordinates(x + i, y + j);
                    nearCells.add(field[y + j][x + i]);
                } catch (GameFieldException ignored){}
            }
        }
        return nearCells;
    }

    private void countNearBombsForAllCells() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = field[row][col];
                cell.setNearBombNumber(countCellNearBombs(cell));
            }
        }
    }

    private void validateCoordinates(int x, int y){
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new GameFieldException("Coordinates are not from valid interval:" +
                    "(x)" + x + ": [" + 0 + ", " + (width - 1) + "], " +
                    "(y)" + y + ": [" + 0 + ", " + (height - 1) + "]");
        }
    }

    private int countCellNearBombs(Cell cell){
        int nearBombsCount = 0;
        int cellX = cell.getX();
        int cellY = cell.getY();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    validateCoordinates(cellX + i, cellY + j);
                    nearBombsCount += (field[cellY + j][cellX + i].getType() == CellType.BOMB) ? 1 : 0;
                } catch (GameFieldException ignored){}
            }
        }
        return nearBombsCount;
    }

    private void generateBombs(int bombsNumber) {
        Random random = new Random();
        int bombCount = 0;
        while (bombCount < bombsNumber){
            Cell randomCell = field[random.nextInt(height)][random.nextInt(width)];
            if (!bombs.contains(randomCell)){
                randomCell.setCellType(CellType.BOMB);
                bombs.add(randomCell);
                bombCount++;
            }
        }
    }

    private void fillField(){
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length; col++) {
                field[row][col] = new Cell(col, row);
            }
        }
    }

    private void validateFieldParams(int width, int height, int bombsNumber){
        if (width <= 0 || width > MAX_FIELD_WIDTH){
            throw new GameFieldException("Width is not from valid interval: [" + 1 + ", " + MAX_FIELD_WIDTH + "]");
        }
        if (height <= 0 || height > MAX_FIELD_HEIGHT){
            throw new GameFieldException("Height is not from valid interval: [" + 1 + ", " + MAX_FIELD_HEIGHT + "]");
        }
        if (bombsNumber <= 0 || bombsNumber >= width * height){
            throw new GameFieldException("Bombs number is not from valid interval: [" + 1 + ", " + (width * height) + "]");
        }
    }
}