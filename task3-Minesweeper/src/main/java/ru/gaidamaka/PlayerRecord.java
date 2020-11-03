package ru.gaidamaka;

import java.io.Serializable;

public class PlayerRecord implements Serializable {
    private final String playerName;
    private final int score;

    public PlayerRecord(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
}