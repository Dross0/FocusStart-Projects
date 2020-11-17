package ru.gaidamaka.highscoretable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.exception.HighScoreTableManagerException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class HighScoreTableManager {
    private static final Logger logger = LoggerFactory.getLogger(HighScoreTableManager.class);

    private static final int DEFAULT_TABLE_CAPACITY = 10;
    private static final HighScoreOrder DEFAULT_TABLE_ORDER = HighScoreOrder.MAX;
    private final Path tableFilePath;
    private final int newTableCapacity;
    private final HighScoreOrder tableOrder;

    @Nullable
    private HighScoreTable table;

    public HighScoreTableManager(@NotNull Path tableFilePath, @NotNull HighScoreOrder tableOrder, int newTableCapacity) {
        this.tableFilePath = Objects.requireNonNull(tableFilePath, "Path must be not null");
        this.tableOrder = Objects.requireNonNull(tableOrder, "Table order must be not null");
        this.newTableCapacity = newTableCapacity;
    }

    public HighScoreTableManager(@NotNull Path tableFilePath, @NotNull HighScoreOrder tableOrder) {
        this(tableFilePath, tableOrder, DEFAULT_TABLE_CAPACITY);
    }

    public HighScoreTableManager(@NotNull Path tableFilePath) {
        this(tableFilePath, DEFAULT_TABLE_ORDER, DEFAULT_TABLE_CAPACITY);
    }

    @NotNull
    public HighScoreTable getOrCreateTable() {
        if (table == null) {
            try {
                read();
            } catch (HighScoreTableManagerException e) {
                logger.warn("Cant read high score table, default table will be used", e);
                table = new HighScoreTable(newTableCapacity, tableOrder);
            }
        }
        return table;
    }

    public void save() {
        if (table == null) {
            return;
        }
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(tableFilePath))) {
            objectOutputStream.writeObject(table);
            objectOutputStream.flush();
            table = null;
        } catch (IOException e) {
            throw new HighScoreTableManagerException("Cant save table", e);
        }
    }

    private void read() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(tableFilePath))) {
            table = (HighScoreTable) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new HighScoreTableManagerException("Cant read high score table", e);
        }
    }
}
