package ru.gaidamaka;

import java.io.Serializable;
import java.util.*;


public class HighScoreTable implements Serializable, Iterable<PlayerRecord> {
    private final List<PlayerRecord> recordList;
    private PlayerRecord minRecord;
    private final int capacity;

    public HighScoreTable(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("High score table size must be > 0");
        }
        this.capacity = capacity;
        recordList = new ArrayList<>(capacity);
    }

    public boolean isHighScore(int score) {
        if (minRecord == null || recordList.size() < capacity) {
            return true;
        }
        return score > minRecord.getScore();
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
        if (newRecord.getScore() > prevRecord.getScore()) {
            recordList.remove(prevRecord);
            recordList.add(newRecord);
        }
    }

    public void addNewRecord(PlayerRecord record) {
        if (!isHighScore(record.getScore())) {
            return;
        }
        Optional<PlayerRecord> thisPrevPlayerRecord = getPlayerRecord(record.getPlayerName());
        if (thisPrevPlayerRecord.isEmpty() && minRecord != null && recordList.size() >= capacity) {
            recordList.remove(minRecord);
        }
        thisPrevPlayerRecord.ifPresentOrElse(
                (prevRecord) -> updateRecord(prevRecord, record),
                () -> recordList.add(record)
        );
        recordList.sort(Comparator.comparingInt(PlayerRecord::getScore));
        minRecord = recordList.get(0);
    }

    @Override
    public Iterator<PlayerRecord> iterator() {
        return recordList.iterator();
    }
}
