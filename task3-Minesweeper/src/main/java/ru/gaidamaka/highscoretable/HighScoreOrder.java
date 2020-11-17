package ru.gaidamaka.highscoretable;

import org.jetbrains.annotations.NotNull;

public enum HighScoreOrder {
    MIN {
        @Override
        public int compareRecords(@NotNull PlayerRecord record1, @NotNull PlayerRecord record2) {
            return record1.compareTo(record2);
        }

        @Override
        public @NotNull PlayerRecord getHigherScoreRecord(@NotNull PlayerRecord record1, @NotNull PlayerRecord record2) {
            return (record1.compareTo(record2) < 0)
                    ? record1 :
                    record2;
        }
    },
    MAX {
        @Override
        public int compareRecords(@NotNull PlayerRecord record1, @NotNull PlayerRecord record2) {
            return record2.compareTo(record1);
        }

        @Override
        public @NotNull PlayerRecord getHigherScoreRecord(@NotNull PlayerRecord record1, @NotNull PlayerRecord record2) {
            return (record1.compareTo(record2) >= 0)
                    ? record1
                    : record2;
        }
    };

    public abstract int compareRecords(@NotNull PlayerRecord record1, @NotNull PlayerRecord record2);

    @NotNull
    public abstract PlayerRecord getHigherScoreRecord(@NotNull PlayerRecord record1, @NotNull PlayerRecord record2);
}
