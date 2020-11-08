package ru.gaidamaka.highscoretable;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class PlayerRecord implements Serializable, Comparable<PlayerRecord>{
    private final String playerName;
    private final int score;

    public PlayerRecord(@NotNull String playerName, int score) {
        this.playerName = Objects.requireNonNull(playerName, "Player name must be not null");
        this.score = score;
    }

    @NotNull
    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerRecord that = (PlayerRecord) o;
        return score == that.score &&
                playerName.equals(that.playerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, score);
    }

    @Override
    public int compareTo(@NotNull PlayerRecord record) {
        return score - record.score;
    }
}