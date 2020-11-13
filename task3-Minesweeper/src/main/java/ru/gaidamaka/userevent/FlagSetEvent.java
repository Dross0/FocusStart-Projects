package ru.gaidamaka.userevent;

public class FlagSetEvent extends UserEvent {
    private final int xCoordinate;
    private final int yCoordinate;

    public FlagSetEvent(int xCoordinate, int yCoordinate) {
        super(UserEventType.FLAG_SET);
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
