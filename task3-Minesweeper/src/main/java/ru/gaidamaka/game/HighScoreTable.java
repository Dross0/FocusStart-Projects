package ru.gaidamaka.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class HighScoreTable implements Serializable {
    private final Map<String, Integer> table;
    private final int capacity;

    public HighScoreTable(int capacity){
        if (capacity <= 0){
            throw new IllegalArgumentException("High score table size must be > 0");
        }
        this.capacity = capacity;
        table = new HashMap<>(capacity);
    }


    public boolean addNewRecord(String name, int score){
        if (table.containsKey(name)){
            if (table.get(name) > score) {
                return false;
            }
            else{
                table.put(name, score);
                return true;
            }
        }
        if (table.size() < capacity){
            table.put(name, score);
            return true;
        }
        Map.Entry<String, Integer> recordWithMinScore = findEntryWithMinScore();
        if (score > recordWithMinScore.getValue()){
            table.put(name, score);
            table.remove(recordWithMinScore.getKey());
            return true;
        }
        return false;
    }

    public Map<String, Integer> getSortedTable(){
        return table.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    }

    private Map.Entry<String, Integer> findEntryWithMinScore(){
        int minScore = Integer.MAX_VALUE;
        Map.Entry<String, Integer> minEntry = null;
        for (Map.Entry<String, Integer> entry: table.entrySet()){
            if (entry.getValue() < minScore){
                minScore = entry.getValue();
                minEntry = entry;
            }
        }
        return minEntry;
    }


}
