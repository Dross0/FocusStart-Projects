package ru.gaidamaka.highscoretable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;


public class HighScoreTable implements Serializable, Iterable<PlayerRecord> {
    private final List<PlayerRecord> recordList; // The highest records are from the beginning of the list
    private final int capacity;
    private final HighScoreOrder tableOrder;

    @Nullable
    private PlayerRecord lowestRecord;

    public HighScoreTable(int capacity, @NotNull HighScoreOrder tableOrder) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("High score table size must be > 0");
        }
        this.tableOrder = Objects.requireNonNull(tableOrder, "Table order cant be null");
        this.capacity = capacity;
        recordList = new ArrayList<>(capacity);
    }

    public boolean isHighScore(int score) {
        if (lowestRecord == null || recordList.size() < capacity) {
            return true;
        }
        PlayerRecord tmpRecord = new PlayerRecord("", score);
        PlayerRecord higherScoreRecord = tableOrder.getHigherScoreRecord(tmpRecord, lowestRecord);
        return tmpRecord.equals(higherScoreRecord);
    }

    public boolean containsPlayer(String playerName) {
        for (PlayerRecord record : recordList) {
            if (record.getPlayerName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    private Optional<PlayerRecord> getPlayerRecord(String playerName) {
        for (PlayerRecord record : recordList) {
            if (record.getPlayerName().equals(playerName)) {
                return Optional.of(record);
            }
        }
        return Optional.empty();
    }

    private void updateRecord(PlayerRecord prevRecord, PlayerRecord newRecord) {
        PlayerRecord higherScoreRecord = tableOrder.getHigherScoreRecord(newRecord, prevRecord);
        if (newRecord.equals(higherScoreRecord)) {
            recordList.remove(prevRecord);
            recordList.add(newRecord);
        }
    }

    public void addNewRecord(@NotNull PlayerRecord record) {
        Objects.requireNonNull(record, "Record cant be a null");
        if (!isHighScore(record.getScore())) {
            return;
        }
        Optional<PlayerRecord> thisPrevPlayerRecord = getPlayerRecord(record.getPlayerName());
        if (thisPrevPlayerRecord.isEmpty() && lowestRecord != null && recordList.size() >= capacity) {
            recordList.remove(lowestRecord);
        }
        thisPrevPlayerRecord.ifPresentOrElse(
                (prevRecord) -> updateRecord(prevRecord, record),
                () -> recordList.add(record)
        );
        recordList.sort((o1, o2) -> tableOrder == HighScoreOrder.MIN
                ? o1.compareTo(o2)
                : -o1.compareTo(o2));
        lowestRecord = recordList.get(recordList.size() - 1);
    }

    @Override
    @NotNull
    public Iterator<PlayerRecord> iterator() {
        return recordList.iterator();
    }
}
