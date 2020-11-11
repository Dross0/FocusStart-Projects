package ru.gaidamaka.ui;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.UserEvent;
import ru.gaidamaka.UserEventType;
import ru.gaidamaka.game.cell.Cell;
import ru.gaidamaka.game.cell.CellType;
import ru.gaidamaka.highscoretable.HighScoreTable;
import ru.gaidamaka.presenter.Presenter;
import ru.gaidamaka.ui.highscore.HighScoreWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MinesweeperView implements View{
    private static final String FONT = "Serif";
    private static final int MAX_SCORE_FIELD_CHAR_NUMBER = 3;
    private static final int MAIN_WINDOW_WIDTH = 400;
    private static final int MAIN_WINDOW_HEIGHT = 550;
    private static final int GAME_FIELD_LAYOUT_HEIGHT = 500;
    private static final int IMAGE_SIZE = 40;
    private final JFrame mainWindow;
    private final GridBagLayout mainWindowLayout;
    private final JPanel gameField;
    private final GridLayout gameFieldView;

    private List<ImageIcon> nearBombsIcons;
    private ImageIcon flagIcon;
    private ImageIcon bombIcon;
    private ImageIcon closedCellIcon;

    private List<JButton> field;
    private final int gameFieldHeight;
    private final int gameFieldWidth;
    private Presenter presenter;
    private final JLabel scoreLabel;
    private final JLabel flagsLabel;


    public MinesweeperView(int gameFieldWidth, int gameFieldHeight){
        this.gameFieldWidth = gameFieldWidth;
        this.gameFieldHeight = gameFieldHeight;
        mainWindow = new JFrame();
        mainWindow.setSize(IMAGE_SIZE * gameFieldWidth, IMAGE_SIZE * gameFieldHeight);
        mainWindow.setTitle("Сапёр");
        mainWindow.setResizable(true);
        mainWindowLayout = new GridBagLayout();
        mainWindow.setLayout(mainWindowLayout);
        gameField = new JPanel();
        //gameField.setSize(IMAGE_SIZE * gameFieldWidth, IMAGE_SIZE * gameFieldHeight);

        gameFieldView = new GridLayout(gameFieldHeight, gameFieldWidth, 0, 0);
        gameField.setLayout(gameFieldView);
        gameFieldView.setHgap(-12);
        gameFieldView.setVgap(-12);

        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font(FONT, Font.BOLD, 20));
        flagsLabel = new JLabel();
        flagsLabel.setFont(new Font(FONT, Font.BOLD, 20));
        updateScoreBoard(0, 0);

        GridBagConstraints c = new GridBagConstraints();
        //c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        mainWindow.add(scoreLabel, c);
        c.gridx = 1;
        mainWindow.add(flagsLabel, c);
        c.gridx = 0;
        c.gridy = 1;
        mainWindow.add(gameField, c);
        initIcons();
        initField();
        initMenu();
        mainWindow.pack();
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
    }

    private void initField(){
        field = new ArrayList<>(gameFieldHeight * gameFieldWidth);
        for (int row = 0; row < gameFieldHeight; row++) {
            for (int col = 0; col < gameFieldWidth; col++) {
                JButton button = new JButton(closedCellIcon);
                field.add(button);
                button.setMargin(new Insets(0, 0, 0, 0));
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        UserEventType type = UserEventType.SHOW_CELL;
                        if (e.getButton() == MouseEvent.BUTTON2){
                            type = UserEventType.SHOW_NEAR_EMPTY_CELLS;
                        }
                        else if (e.getButton() == MouseEvent.BUTTON1){
                            type = UserEventType.SHOW_CELL;
                        }
                        else if (e.getButton() == MouseEvent.BUTTON3){
                            type = UserEventType.FLAG_SET;
                        }
                        fireEvent(new UserEvent(type, field.indexOf(button) % gameFieldWidth,
                                field.indexOf(button) / gameFieldWidth
                        ));
                    }
                });
                gameField.add(button);
            }
        }
    }

    private ImageIcon readImage(String imageName){
        return new ImageIcon(Objects.requireNonNull(
                MinesweeperView.class.getClassLoader().getResource(imageName)),
                "Wrong resource name"
        );
    }

    private ImageIcon readAndResizeImage(String imageName){
        Image scaledImage = readImage(imageName)
                .getImage()
                .getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_DEFAULT);
        return new ImageIcon(scaledImage);
    }

    private void initIcons(){
        nearBombsIcons = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            nearBombsIcons.add(readAndResizeImage(i + ".png"));
        }
        closedCellIcon = readAndResizeImage("closedCell.png");
        bombIcon = readAndResizeImage("bomb.png");
        flagIcon = readAndResizeImage("flag.png");
    }

    private void initMenu(){
        JMenuBar menu = new JMenuBar();
        JMenuItem newGameMenuItem = new JMenuItem("Новая игра");
        newGameMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1){
                    presenter.onEvent(new UserEvent(UserEventType.NEW_GAME));
                }
            }
        });
        JMenuItem highScoreMenuItem = new JMenuItem("Таблица рекордов");
        highScoreMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    presenter.onEvent(new UserEvent(UserEventType.SHOW_HIGH_SCORE_TABLE));
                }
            }
        });
        JMenuItem exitGameMenuItem = new JMenuItem("Выход");
        exitGameMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    presenter.onEvent(new UserEvent(UserEventType.EXIT_GAME));
                    System.exit(0); //FIXME Избавиться от exit
                }
            }
        });
        menu.add(newGameMenuItem);
        menu.add(highScoreMenuItem);
        menu.add(exitGameMenuItem);
        mainWindow.setJMenuBar(menu);
    }

    @Override
    public void drawCell(@NotNull Cell cell) {
        Objects.requireNonNull(cell, "Cell cant be null");
        JButton relButton = field.get(cell.getY() * gameFieldWidth + cell.getX());
        if (cell.isMarked()){
            relButton.setIcon(flagIcon);
            return;
        }
        if (cell.isHidden()){
            relButton.setIcon(closedCellIcon);
        } else{
            if (cell.getType() == CellType.BOMB){
                relButton.setIcon(bombIcon);
                System.out.println(1);
            } else{
                relButton.setIcon(nearBombsIcons.get(cell.getNearBombNumber()));
            }
        }
    }

    @Override
    public void fireEvent(UserEvent event) {
        presenter.onEvent(event);
    }

    private void showDialogWithCenterLabel(JLabel label, int gap) {
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout(gap, gap));
        content.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));
        content.add(label);
        dialog.add(content);
        dialog.setVisible(true);
        dialog.pack();
    }

    @Override
    public void showLoseScreen() {
        JLabel loseLabel = new JLabel("Вы проиграли!");
        loseLabel.setFont(new Font(FONT, Font.BOLD, 30));
        showDialogWithCenterLabel(loseLabel, 30);
    }

    @Override
    public void showWinScreen() {
        JLabel winLabel = new JLabel("Вы проиграли!");
        winLabel.setFont(new Font(FONT, Font.BOLD, 30));
        showDialogWithCenterLabel(winLabel, 30);
    }

    @Override
    public void showHighScoreTable(@NotNull HighScoreTable table) {
        Objects.requireNonNull(table, "Table cant be null");
        HighScoreWindow highScoreWindow = new HighScoreWindow(table, new Font(FONT, Font.BOLD, 20));
        highScoreWindow.show();
    }

    private String formatScore(int score){
        int charsAtScore = String.valueOf(score).length();
        return "0".repeat(MAX_SCORE_FIELD_CHAR_NUMBER - charsAtScore) + score;
    }

    @Override
    public void updateScoreBoard(int score, int flagsNumber) {
        scoreLabel.setText(formatScore(score));
        flagsLabel.setText(String.valueOf(flagsNumber));
    }

    @Override
    @NotNull
    public String readPlayerName() {
        JPanel userNameInputPanel = new JPanel();
        userNameInputPanel.setLayout(new BorderLayout());
        JTextField userNameInputField = new JTextField(30);
        userNameInputPanel.add(new JLabel("Ваше имя: "), BorderLayout.LINE_START);
        userNameInputPanel.add(userNameInputField);
        JOptionPane.showMessageDialog(mainWindow,
                userNameInputPanel,
                "Новый рекорд",
                JOptionPane.QUESTION_MESSAGE,
                readImage("recordDialog.png")
        );
        return userNameInputField.getText();
    }

    @Override
    public void setPresenter(@NotNull Presenter presenter) {
        this.presenter = Objects.requireNonNull(presenter, "Presenter cant be null");
    }
}
