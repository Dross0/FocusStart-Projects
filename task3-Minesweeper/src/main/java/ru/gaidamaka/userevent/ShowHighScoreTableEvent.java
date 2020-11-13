package ru.gaidamaka.userevent;

public class ShowHighScoreTableEvent extends UserEvent {
    public ShowHighScoreTableEvent() {
        super(UserEventType.SHOW_HIGH_SCORE_TABLE);
    }
}
