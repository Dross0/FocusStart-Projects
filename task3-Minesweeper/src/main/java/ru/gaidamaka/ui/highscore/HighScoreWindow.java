package ru.gaidamaka.ui.highscore;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.highscoretable.HighScoreTable;
import ru.gaidamaka.highscoretable.PlayerRecord;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class HighScoreWindow {
    private static final int COLUMN_INTERNAL_PADDING = 100;
    private final HighScoreTable table;
    private final JDialog window;
    private final Font headerFont;

    public HighScoreWindow(@NotNull HighScoreTable table, @NotNull Font headerFont) {
        this.table = Objects.requireNonNull(table, "High score table cant be null");
        this.headerFont = Objects.requireNonNull(headerFont, "Header font cant be null");
        this.window = new JDialog();
        window.setLayout(new GridBagLayout());
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        fillTableView();
    }

    private void fillTableView() {
        GridBagConstraints indexColumnSettings = generateColumnSettings(0);
        GridBagConstraints playerNamesColumnSettings = generateColumnSettings(1);
        GridBagConstraints scoresColumnSettings = generateColumnSettings(2);
        addHeaderOfColumn("Номер", indexColumnSettings);
        addHeaderOfColumn("Имя", playerNamesColumnSettings);
        addHeaderOfColumn("Очки", scoresColumnSettings);
        int recordIndex = 1;
        for (PlayerRecord record : table) {
            addRowAtColumn(new JLabel(String.valueOf(recordIndex)), indexColumnSettings);
            addRowAtColumn(new JLabel(record.getPlayerName()), playerNamesColumnSettings);
            addRowAtColumn(new JLabel(String.valueOf(record.getScore())), scoresColumnSettings);
            recordIndex++;
        }
    }

    private void addHeaderOfColumn(String headerText, GridBagConstraints columnSettings){
        JLabel header = new JLabel(headerText);
        header.setFont(headerFont);
        addRowAtColumn(header, columnSettings);
    }

    private void addRowAtColumn(JLabel label, GridBagConstraints columnSettings){
        window.add(label, columnSettings);
        columnSettings.gridy++;
    }

    private GridBagConstraints generateColumnSettings(int columnIndex){
        GridBagConstraints column = new GridBagConstraints();
        column.ipadx = COLUMN_INTERNAL_PADDING;
        column.gridy = 0;
        column.gridx = columnIndex;
        return column;
    }

    public void show(){
        window.pack();
        window.setVisible(true);
    }
}
