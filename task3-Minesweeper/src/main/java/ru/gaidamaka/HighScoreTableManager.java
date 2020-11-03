package ru.gaidamaka;

import ru.gaidamaka.exception.HighScoreTableManagerException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class HighScoreTableManager  {
    private static final int DEFAULT_TABLE_CAPACITY = 10;
    private HighScoreTable table;
    private final Path tableFilePath;
    private final int newTableCapacity;

    public HighScoreTableManager(Path tableFilePath, int newTableCapacity){
        this.tableFilePath = tableFilePath;
        this.newTableCapacity = newTableCapacity;
    }

    public HighScoreTableManager(Path tableFilePath){
        this(tableFilePath, DEFAULT_TABLE_CAPACITY);
    }

    public HighScoreTable getOrCreateTable() {
        if (table == null){
            table = new HighScoreTable(newTableCapacity);
        }
        return table;
    }

    public void save() throws HighScoreTableManagerException {
        if (table == null){
            return;
        }
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(tableFilePath))){
            objectOutputStream.writeObject(table);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new HighScoreTableManagerException("Cant save table", e);
        }
    }

    public void read() throws HighScoreTableManagerException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(tableFilePath))){
            table = (HighScoreTable) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            table = new HighScoreTable(newTableCapacity);
            throw new HighScoreTableManagerException("Cant read high score table", e);
        }
    }
}
