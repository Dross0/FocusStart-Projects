package ru.gaidamaka;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.exception.GameFieldException;
import ru.gaidamaka.exception.ImageReadException;
import ru.gaidamaka.game.Game;
import ru.gaidamaka.highscoretable.HighScoreOrder;
import ru.gaidamaka.highscoretable.HighScoreTableManager;
import ru.gaidamaka.presenter.MinesweeperPresenter;
import ru.gaidamaka.ui.MinesweeperView;
import ru.gaidamaka.ui.View;

import java.nio.file.Path;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final int DEFAULT_FIELD_WIDTH = 10;
    private static final int DEFAULT_FIELD_HEIGHT = 10;
    private static final int DEFAULT_BOMBS_NUMBER = 10;
    private static final int HIGH_SCORE_TABLE_CAPACITY = 10;
    private static final Path HIGH_SCORE_TABLE_PATH = Path.of("userRecords.data");
    private static final String ABOUT_GAME_FILE_PATH = "about.txt";

    public static void main(String[] args) {
        try {
            Game game = new Game(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT, DEFAULT_BOMBS_NUMBER);
            View view = new MinesweeperView(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT);
            HighScoreTableManager manager = new HighScoreTableManager(HIGH_SCORE_TABLE_PATH, HighScoreOrder.MIN, HIGH_SCORE_TABLE_CAPACITY);
            MinesweeperPresenter presenter = new MinesweeperPresenter(game, view, manager);
            presenter.setAboutGameInputStream(Main.class.getClassLoader().getResourceAsStream(ABOUT_GAME_FILE_PATH));
            presenter.runGame();
        } catch (ImageReadException e) {
            logger.error(e.getMessage(), e);
        } catch (GameFieldException e) {
            logger.error("Invalid default field sizes", e);
        }
    }
}
