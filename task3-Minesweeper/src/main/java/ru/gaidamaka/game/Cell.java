package ru.gaidamaka.game;

import java.util.Objects;

public class Cell {
    private final int x;
    private final int y;
    private CellType cellType;
    private boolean isHiddenFlag;
    private boolean isMarkedFlag;
    private int nearBombNumber;

    public Cell(int x, int y, CellType cellType) {
        this.x = x;
        this.y = y;
        this.cellType = cellType;
        this.isHiddenFlag = true;
        this.isMarkedFlag = false;
        this.nearBombNumber = 0;
    }

    public Cell(int x, int y){
        this(x, y, CellType.EMPTY);
    }

    public void setNearBombNumber(int nearBombNumber){
        setCellType((nearBombNumber > 0) ? CellType.NEAR_BOMB : CellType.EMPTY);
        this.nearBombNumber = nearBombNumber;
    }

    public int getNearBombNumber() {
        return nearBombNumber;
    }

    public void show(){
        isHiddenFlag = false;
    }

    public void hide(){
        isHiddenFlag = true;
    }


    public boolean isHidden(){
        return isHiddenFlag;
    }

    public boolean isMarked(){
        return isMarkedFlag;
    }

    public void mark(){
        isMarkedFlag = true;
    }

    public void unmark(){
        isMarkedFlag = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CellType getType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x &&
                y == cell.y &&
                isHiddenFlag == cell.isHiddenFlag &&
                cellType == cell.cellType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }


}
