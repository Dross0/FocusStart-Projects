package ru.gaidamaka.game.cell;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Cell {
    private final int x;
    private final int y;
    private CellType cellType;
    private boolean isHiddenFlag;
    private boolean isMarkedFlag;
    private int nearBombNumber;

    public Cell(int x, int y, @NotNull CellType cellType) {
        this.cellType = Objects.requireNonNull(cellType, "Cell type cant be null");
        this.x = x;
        this.y = y;
        this.isHiddenFlag = true;
        this.isMarkedFlag = false;
        this.nearBombNumber = 0;
    }

    public Cell(int x, int y){
        this(x, y, CellType.EMPTY);
    }

    public Cell(@NotNull Cell cell){
        this(cell.x, cell.y, cell.cellType);
        this.isHiddenFlag = cell.isHiddenFlag;
        this.isMarkedFlag = cell.isMarkedFlag;
        this.nearBombNumber = cell.nearBombNumber;
    }

    public void setNearBombNumber(int nearBombNumber){
        if (cellType == CellType.BOMB) {
            return;
        }
        this.cellType = (nearBombNumber > 0)
                ? CellType.NEAR_BOMB
                : CellType.EMPTY;
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

    @NotNull
    public CellType getType() {
        return cellType;
    }

    public void setCellType(@NotNull CellType cellType) {
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
