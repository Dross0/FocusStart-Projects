package ru.gaidamaka.ui.gamefield;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.userevent.FlagSetEvent;
import ru.gaidamaka.userevent.ShowCellEvent;
import ru.gaidamaka.userevent.ShowNearEmptyCellsEvent;
import ru.gaidamaka.userevent.UserEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class GameFieldView {

    private int buttonPreferredWidth;
    private int buttonPreferredHeight;

    private final JPanel gameField;
    private final GridLayout fieldLayout;
    private ArrayList<JButton> field;
    private int fieldWidth;

    public GameFieldView() {
        this.gameField = new JPanel(new GridLayout());
        this.fieldLayout = new GridLayout();
        this.buttonPreferredHeight = 0;
        this.buttonPreferredWidth = 0;
        gameField.setLayout(fieldLayout);
    }

    public void setButtonPreferredHeight(int buttonPreferredHeight) {
        this.buttonPreferredHeight = buttonPreferredHeight;
    }

    public void setButtonPreferredWidth(int buttonPreferredWidth) {
        this.buttonPreferredWidth = buttonPreferredWidth;
    }

    @NotNull
    public JPanel getGameFieldPanel() {
        return gameField;
    }

    public void reset(int width, int height, @NotNull Consumer<UserEvent> eventConsumer) {
        Objects.requireNonNull(eventConsumer, "Consumer cant be null");
        fieldWidth = width;
        fieldLayout.setRows(height);
        fieldLayout.setColumns(width);
        gameField.removeAll();
        field = new ArrayList<>(width * height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(buttonPreferredWidth, buttonPreferredHeight));
                button.setBorderPainted(false);
                button.setContentAreaFilled(false);
                field.add(button);
                int xCoordinate = col;
                int yCoordinate = row;
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON2) {
                            eventConsumer.accept(new ShowNearEmptyCellsEvent(xCoordinate, yCoordinate));
                        } else if (e.getButton() == MouseEvent.BUTTON1) {
                            eventConsumer.accept(new ShowCellEvent(xCoordinate, yCoordinate));
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            eventConsumer.accept(new FlagSetEvent(xCoordinate, yCoordinate));
                        }
                    }
                });
                gameField.add(button);
            }
        }
    }

    public void updateCell(int x, int y, @NotNull ImageIcon cellImage) {
        field.get(y * fieldWidth + x)
                .setIcon(Objects.requireNonNull(cellImage, "Cell image cant be null"));
    }


}
