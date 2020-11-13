package ru.gaidamaka.userevent;


public class NewGameEvent extends UserEvent {
    private final int fieldWidth;
    private final int fieldHeight;
    private final int bombsNumber;

    public NewGameEvent(int fieldWidth, int fieldHeight, int bombsNumber) {
        super(UserEventType.NEW_GAME);
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.bombsNumber = bombsNumber;
    }

    public int getBombsNumber() {
        return bombsNumber;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public int getFieldWidth() {
        return fieldWidth;
    }
}
