package ru.gaidamaka;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UserEvent {
    private final UserEventType type;
    private final int xCoordinate;
    private final int yCoordinate;


    public UserEvent(@NotNull UserEventType type, int xCoordinate, int yCoordinate) {
        this.type = Objects.requireNonNull(type, "User event type cant be null");
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public UserEvent(@NotNull UserEventType type){
        this(type, -1, -1);
    }

    @NotNull
    public UserEventType getType() {
        return type;
    }

    public int getX() {
        return xCoordinate;
    }

    public int getY(){
        return yCoordinate;
    }
}
