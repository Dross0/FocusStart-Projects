package ru.gaidamaka.presenter;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.GameObserver;
import ru.gaidamaka.exception.GameFieldException;
import ru.gaidamaka.exception.HighScoreTableManagerException;
import ru.gaidamaka.game.Game;
import ru.gaidamaka.game.event.GameEvent;
import ru.gaidamaka.game.event.GameEventType;
import ru.gaidamaka.highscoretable.HighScoreTableManager;
import ru.gaidamaka.highscoretable.PlayerRecord;
import ru.gaidamaka.timer.Timer;
import ru.gaidamaka.ui.View;
import ru.gaidamaka.userevent.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class MinesweeperPresenter implements Presenter, GameObserver {
    private static final Logger logger = LoggerFactory.getLogger(MinesweeperPresenter.class);
    private static final String DEFAULT_ABOUT_TEXT = "Игра сапер";

    private final View view;
    private final Game game;
    private final Timer timer;
    private final HighScoreTableManager highScoreTableManager;
    private InputStream aboutGameInputStream = null;
    private String aboutGameText = null;

    public MinesweeperPresenter(@NotNull Game game,
                                @NotNull View view,
                                @NotNull HighScoreTableManager manager) {
        this.game = Objects.requireNonNull(game, "Game cant be null");
        this.view = Objects.requireNonNull(view, "View cant be null");
        this.highScoreTableManager = Objects.requireNonNull(manager, "High score table manager cant be null");
        this.timer = new Timer();
        timer.addTickCallback(game::setCurrentScore);
        timer.addTickCallback(view::updateScore);
        game.addObserver(this);
        view.setPresenter(this);
    }

    public void setAboutGameInputStream(InputStream aboutGameInputStream) {
        this.aboutGameInputStream = aboutGameInputStream;
    }

    public void finishGame() {
        game.exit();
        timer.stop();
    }

    @Override
    public void onEvent(@NotNull UserEvent event) {
        Objects.requireNonNull(event, "User event cant be null");
        switch (event.getType()) {
            case EXIT_GAME:
                finishGame();
                break;
            case FLAG_SET:
                handle((FlagSetEvent) event);
                break;
            case SHOW_CELL:
                handle((ShowCellEvent) event);
                break;
            case SHOW_NEAR_EMPTY_CELLS:
                handle((ShowNearEmptyCellsEvent) event);
                break;
            case NEW_GAME:
                handle((NewGameEvent) event);
                break;
            case SHOW_HIGH_SCORE_TABLE:
                showHighScoreTable();
                break;
            case SHOW_ABOUT:
                showAbout();
                break;
            default:
                logger.error("Unknown user event type={}", event.getType());
                throw new IllegalArgumentException("Unknown user event type=" + event.getType());
        }
    }

    private void showAbout() {
        view.showAbout(readAboutText()
                .orElse(DEFAULT_ABOUT_TEXT)
        );
    }

    private void showHighScoreTable() {
        view.showHighScoreTable(highScoreTableManager.getOrCreateTable());
    }

    private void handle(NewGameEvent newGameEvent) {
        restartGame(
                newGameEvent.getFieldWidth(),
                newGameEvent.getFieldHeight(),
                newGameEvent.getBombsNumber()
        );
    }

    private void handle(ShowNearEmptyCellsEvent nearEmptyCellsEvent) {
        game.showNeighborsOfOpenCell(nearEmptyCellsEvent.getX(), nearEmptyCellsEvent.getY());
    }

    private void handle(ShowCellEvent showCellEvent) {
        if (timer.isPaused()) {
            timer.start();
        }
        game.showCell(showCellEvent.getX(), showCellEvent.getY());
    }

    private void handle(FlagSetEvent flagSetEvent) {
        game.toggleMarkCell(flagSetEvent.getX(), flagSetEvent.getY());
    }

    private Optional<String> readAboutText() {
        if (aboutGameInputStream == null) {
            return Optional.empty();
        }
        if (aboutGameText == null) {
            StringBuilder aboutGameSB = new StringBuilder();
            try (Scanner scanner = new Scanner(aboutGameInputStream, StandardCharsets.UTF_8)) {
                while (scanner.hasNextLine()) {
                    aboutGameSB.append(scanner.nextLine()).append('\n');
                }
            }
            aboutGameText = aboutGameSB.toString();
        }
        return Optional.of(aboutGameText);
    }

    private void restartGame(int fieldWidth, int fieldHeight, int bombsNumber) {
        try {
            game.reset(
                    fieldWidth,
                    fieldHeight,
                    bombsNumber
            );
            view.updateFlagsNumber(bombsNumber);
        } catch (GameFieldException e) {
            logger.error("Wrong field parameters", e);
            view.showErrorMessage("Недопустимые параметры поля");
            finishGame();
            return;
        }
        runGame();
    }

    public void runGame() {
        game.run();
    }

    private void showResultWindow(GameEvent gameEvent) {
        if (gameEvent.getEventType() == GameEventType.WIN) {
            if (highScoreTableManager.getOrCreateTable().isHighScore(gameEvent.getScore())) {
                String playerName = view.readPlayerName();
                highScoreTableManager
                        .getOrCreateTable()
                        .addNewRecord(new PlayerRecord(
                                playerName,
                                gameEvent.getScore())
                        );
            }
            view.showWinScreen();
            try {
                highScoreTableManager.save();
            } catch (HighScoreTableManagerException e) {
                logger.warn("Cant save high score table", e);
            }
        } else if (gameEvent.getEventType() == GameEventType.LOSE) {
            view.showLoseScreen();
        }
    }


    @Override
    public void update(@NotNull GameEvent gameEvent) {
        Objects.requireNonNull(gameEvent, "Game event cant be null");
        final GameEventType eventType = gameEvent.getEventType();
        if (eventType == GameEventType.START_GAME) {
            timer.pause();
        } else if (eventType == GameEventType.WIN || eventType == GameEventType.LOSE) {
            timer.pause();
            showResultWindow(gameEvent);
        }
        view.updateFlagsNumber(gameEvent.getCurrentBombsNumberWithoutMarkedCells());
        gameEvent.getUpdatedCells().forEach(view::drawCell);
    }
}
