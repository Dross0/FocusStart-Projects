package ru.gaidamaka.highscoretable;

import org.jetbrains.annotations.NotNull;

public enum HighScoreOrder {
    MIN,
    MAX;

    @NotNull
    public PlayerRecord getHigherScoreRecord(@NotNull PlayerRecord record1, @NotNull PlayerRecord record2){
        switch (this){
            case MAX:
                return (record1.compareTo(record2) >= 0)
                        ? record1
                        : record2;
            case MIN:
                return (record1.compareTo(record2) < 0)
                        ? record1
                        : record2;
            default:
                assert false : "Cant process this order type";
        }
        return record1;
    }
}
