package ru.gaidamaka.userevent;


public class ShowCellEvent extends UserEvent {
    private final int xCoordinate;
    private final int yCoordinate;

    public ShowCellEvent(int xCoordinate, int yCoordinate) {
        super(UserEventType.SHOW_CELL);
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
