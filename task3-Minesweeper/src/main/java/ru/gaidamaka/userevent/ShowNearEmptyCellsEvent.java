package ru.gaidamaka.userevent;


public class ShowNearEmptyCellsEvent extends UserEvent {
    private final int xCoordinate;
    private final int yCoordinate;

    public ShowNearEmptyCellsEvent(int xCoordinate, int yCoordinate) {
        super(UserEventType.SHOW_NEAR_EMPTY_CELLS);
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public int getX() {
        return xCoordinate;
    }

    public int getY() {
        return yCoordinate;
    }
}
