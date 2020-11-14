package ru.gaidamaka.ui;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.exception.ImageReadException;
import ru.gaidamaka.game.cell.Cell;
import ru.gaidamaka.game.cell.CellType;
import ru.gaidamaka.highscoretable.HighScoreTable;
import ru.gaidamaka.presenter.Presenter;
import ru.gaidamaka.ui.gamefield.GameFieldView;
import ru.gaidamaka.ui.highscore.HighScoreWindow;
import ru.gaidamaka.userevent.ExitGameEvent;
import ru.gaidamaka.userevent.NewGameEvent;
import ru.gaidamaka.userevent.ShowHighScoreTableEvent;
import ru.gaidamaka.userevent.UserEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MinesweeperView implements View {
    private static final String FONT = "Serif";
    private static final int MAX_SCORE_FIELD_CHAR_NUMBER = 3;
    private static final int IMAGE_SIZE = 40;
    private static final int MAX_FIELD_WIDTH_SIZE = 32;
    private static final int MAX_FIELD_HEIGHT_SIZE = 32;
    private static final int MAX_BOMBS_NUMBER_SIZE = 32;

    private final JFrame mainWindow;
    private GameFieldView gameFieldView;

    private List<ImageIcon> nearBombsIcons;
    private ImageIcon flagIcon;
    private ImageIcon bombIcon;
    private ImageIcon closedCellIcon;
    private ImageIcon newGameConfigIcon;
    private ImageIcon highScoreWindowIcon;

    private Presenter presenter;
    private JLabel scoreLabel;
    private JLabel flagsLabel;


    public MinesweeperView(int gameFieldWidth, int gameFieldHeight) {
        mainWindow = new JFrame();
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainWindow.setTitle("Сапёр");
        mainWindow.setResizable(true);
        mainWindow.setLayout(new GridBagLayout());
        initScoreBar();
        initGameField(gameFieldWidth, gameFieldHeight);
        initMenu();
        try {
            initIcons();
        } catch (ImageReadException e) {
            showErrorMessage("Технические неполадки: Отсутсвуют необходимые иконки");
            throw e;
        }
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    public void showErrorMessage(@NotNull String errorMessage) {
        JPanel contentPanel = new JPanel();
        JLabel errorMessageLabel = new JLabel(errorMessage);
        contentPanel.add(errorMessageLabel);
        JOptionPane.showMessageDialog(
                mainWindow,
                contentPanel,
                "Ошибка",
                JOptionPane.ERROR_MESSAGE
        );
        closeApp();
    }

    private void closeApp() {
        mainWindow.setVisible(false);
        mainWindow.dispatchEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING));
    }

    private void initGameField(int gameFieldWidth, int gameFieldHeight) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 1;
        c.gridwidth = 2;
        gameFieldView = new GameFieldView();
        gameFieldView.setButtonPreferredWidth(IMAGE_SIZE);
        gameFieldView.setButtonPreferredHeight(IMAGE_SIZE);
        gameFieldView.reset(gameFieldWidth, gameFieldHeight, this::fireEvent);
        mainWindow.add(gameFieldView.getGameFieldPanel(), c);
    }


    private void initScoreBar() {
        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font(FONT, Font.BOLD, 20));
        flagsLabel = new JLabel();
        flagsLabel.setFont(new Font(FONT, Font.BOLD, 20));
        updateScoreBoard(0, 0);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        mainWindow.add(scoreLabel, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        mainWindow.add(flagsLabel, c);
    }

    private ImageIcon readImage(String imageName) {
        URL imageRes = MinesweeperView.class.getClassLoader().getResource(imageName);
        if (imageRes == null) {
            throw new ImageReadException("Cant read image from = {" + imageName + "}");
        }
        return new ImageIcon(imageRes);
    }

    private ImageIcon readAndResizeImage(String imageName){
        Image scaledImage = readImage(imageName)
                .getImage()
                .getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_DEFAULT);
        return new ImageIcon(scaledImage);
    }

    private void initIcons() {
        nearBombsIcons = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            nearBombsIcons.add(readAndResizeImage(i + ".png"));
        }
        closedCellIcon = readAndResizeImage("closedCell.png");
        bombIcon = readAndResizeImage("bomb.png");
        flagIcon = readAndResizeImage("flag.png");
        newGameConfigIcon = readImage("newGameSettings.png");
        highScoreWindowIcon = readImage("recordDialog.png");
    }

    private void initMenu(){
        JMenuBar menu = new JMenuBar();
        JMenuItem newGameMenuItem = new JMenuItem("Новая игра");
        newGameMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1){
                    newGameStart();
                }
            }
        });
        JMenuItem highScoreMenuItem = new JMenuItem("Таблица рекордов");
        highScoreMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    presenter.onEvent(new ShowHighScoreTableEvent());
                }
            }
        });
        JMenuItem exitGameMenuItem = new JMenuItem("Выход");
        exitGameMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    presenter.onEvent(new ExitGameEvent());
                    closeApp();
                }
            }
        });
        menu.add(newGameMenuItem);
        menu.add(highScoreMenuItem);
        menu.add(exitGameMenuItem);
        mainWindow.setJMenuBar(menu);
    }

    private JSlider createNewGameConfigSlider(int min, int max, int defaultValue, int majorTickSpacing) {
        JSlider slider = new JSlider(SwingConstants.HORIZONTAL, min, max, defaultValue);
        slider.setMajorTickSpacing(majorTickSpacing);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    private void newGameStart() {
        JPanel newGameConfigurationPanel = new JPanel();
        newGameConfigurationPanel.setLayout(new BoxLayout(newGameConfigurationPanel, BoxLayout.Y_AXIS));

        JLabel fieldWidthLabel = new JLabel("Ширина поля");
        JSlider fieldWidthSlider = createNewGameConfigSlider(1, MAX_FIELD_WIDTH_SIZE, 10, 10);
        newGameConfigurationPanel.add(fieldWidthLabel);
        newGameConfigurationPanel.add(fieldWidthSlider);

        JLabel fieldHeightLabel = new JLabel("Высота поля");
        JSlider fieldHeightSlider = createNewGameConfigSlider(1, MAX_FIELD_HEIGHT_SIZE, 10, 10);
        newGameConfigurationPanel.add(fieldHeightLabel);
        newGameConfigurationPanel.add(fieldHeightSlider);

        JLabel bombsNumberLabel = new JLabel("Количество бомб");
        JSlider bombsNumberSlider = createNewGameConfigSlider(1, MAX_BOMBS_NUMBER_SIZE, 10, 10);
        newGameConfigurationPanel.add(bombsNumberLabel);
        newGameConfigurationPanel.add(bombsNumberSlider);

        showMessageDialog(
                newGameConfigurationPanel,
                "Новая игра",
                newGameConfigIcon
        );

        int fieldWidth = fieldWidthSlider.getValue();
        int fieldHeight = fieldHeightSlider.getValue();
        int bombsNumber = bombsNumberSlider.getValue();
        gameFieldView.reset(fieldWidth, fieldHeight, this::fireEvent);
        mainWindow.pack();
        presenter.onEvent(new NewGameEvent(
                fieldWidth,
                fieldHeight,
                bombsNumber
        ));
    }

    @Override
    public void drawCell(@NotNull Cell cell) {
        Objects.requireNonNull(cell, "Cell cant be null");
        if (cell.isMarked()) {
            gameFieldView.updateCell(cell.getX(), cell.getY(), flagIcon);
            return;
        }
        if (cell.isHidden()) {
            gameFieldView.updateCell(cell.getX(), cell.getY(), closedCellIcon);
        } else{
            if (cell.getType() == CellType.BOMB){
                gameFieldView.updateCell(cell.getX(), cell.getY(), bombIcon);
            } else{
                gameFieldView.updateCell(cell.getX(), cell.getY(), nearBombsIcons.get(cell.getNearBombNumber()));
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
        JLabel winLabel = new JLabel("Вы выиграли!");
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
        do {
            showMessageDialog(
                    userNameInputPanel,
                    "Вы попали в таблицу рекордов",
                    highScoreWindowIcon
            );
        } while (userNameInputField.getText().isBlank());
        return userNameInputField.getText();
    }

    private void showMessageDialog(JPanel panel, String title, ImageIcon image) {
        JOptionPane.showMessageDialog(mainWindow,
                panel,
                title,
                JOptionPane.QUESTION_MESSAGE,
                image
        );
    }

    @Override
    public void setPresenter(@NotNull Presenter presenter) {
        this.presenter = Objects.requireNonNull(presenter, "Presenter cant be null");
    }
}
