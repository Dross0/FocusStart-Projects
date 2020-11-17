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
import ru.gaidamaka.timer.TimerObserver;
import ru.gaidamaka.ui.View;
import ru.gaidamaka.userevent.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class MinesweeperPresenter implements Presenter, GameObserver, TimerObserver {
    private static final Logger logger = LoggerFactory.getLogger(MinesweeperPresenter.class);
    private static final String DEFAULT_ABOUT_TEXT = "Игра сапер";

    private final View view;
    private final Game game;
    private final Timer timer;
    private final HighScoreTableManager highScoreTableManager;
    private int secondsAfterStartGame;
    private boolean isAllFieldClose = true;
    private InputStream aboutGameInputStream = null;
    private String aboutGameText = null;
    private int currentBombsNumberWithoutMarkedCells;

    public MinesweeperPresenter(@NotNull Game game,
                                @NotNull View view,
                                @NotNull HighScoreTableManager manager) {
        this.game = Objects.requireNonNull(game, "Game cant be null");
        this.view = Objects.requireNonNull(view, "View cant be null");
        this.highScoreTableManager = Objects.requireNonNull(manager, "High score table manager cant be null");
        this.timer = new Timer();
        timer.addObserver(this);
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
                FlagSetEvent flagSetEvent = (FlagSetEvent) event;
                game.toggleMarkCell(flagSetEvent.getX(), flagSetEvent.getY());
                break;
            case SHOW_CELL:
                if (isAllFieldClose) {
                    isAllFieldClose = false;
                    secondsAfterStartGame = 0;
                }
                ShowCellEvent showCellEvent = (ShowCellEvent) event;
                game.showCell(showCellEvent.getX(), showCellEvent.getY());
                break;
            case SHOW_NEAR_EMPTY_CELLS:
                ShowNearEmptyCellsEvent nearEmptyCellsEvent = (ShowNearEmptyCellsEvent) event;
                game.showNeighborsOfOpenCell(nearEmptyCellsEvent.getX(), nearEmptyCellsEvent.getY());
                break;
            case NEW_GAME:
                NewGameEvent newGameEvent = (NewGameEvent) event;
                restartGame(
                        newGameEvent.getFieldWidth(),
                        newGameEvent.getFieldHeight(),
                        newGameEvent.getBombsNumber()
                );
                isAllFieldClose = true;
                break;
            case SHOW_HIGH_SCORE_TABLE:
                view.showHighScoreTable(highScoreTableManager.getOrCreateTable());
                break;
            case SHOW_ABOUT:
                view.showAbout(readAboutText()
                        .orElse(DEFAULT_ABOUT_TEXT)
                );
                break;
        }
    }

    private Optional<String> readAboutText() {
        if (aboutGameInputStream == null) {
            return Optional.empty();
        }
        if (aboutGameText == null) {
            Scanner scanner = new Scanner(aboutGameInputStream, StandardCharsets.UTF_8);
            StringBuilder aboutGameSB = new StringBuilder();
            while (scanner.hasNextLine()) {
                aboutGameSB.append(scanner.nextLine()).append('\n');

            }
            scanner.close();
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
            secondsAfterStartGame = 0;
            view.updateScoreBoard(secondsAfterStartGame, bombsNumber);
        } catch (GameFieldException e) {
            logger.error("Wrong field parameters", e);
            view.showErrorMessage("Недопустимые параметры поля");
            finishGame();
            return;
        }
        runGame();
    }

    public void runGame() {
        secondsAfterStartGame = 0;
        updateGameHighScoreTable();
        game.run();
    }

    private void showResultWindow(GameEvent gameEvent) {
        if (gameEvent.getEventType() == GameEventType.WIN) {
            if (gameEvent.isNewHighScore()) {
                String playerName = view.readPlayerName();
                highScoreTableManager
                        .getOrCreateTable()
                        .addNewRecord(new PlayerRecord(playerName, gameEvent.getScore())
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
            timer.start();
        } else if (eventType == GameEventType.WIN || eventType == GameEventType.LOSE) {
            showResultWindow(gameEvent);
        }
        currentBombsNumberWithoutMarkedCells = gameEvent.getCurrentBombsNumberWithoutMarkedCells();
        view.updateScoreBoard(gameEvent.getScore(), currentBombsNumberWithoutMarkedCells);
        gameEvent.getUpdatedCells().forEach(view::drawCell);

    }

    private void updateGameHighScoreTable() {
        game.setHighScoreTable(highScoreTableManager.getOrCreateTable());
    }

    @Override
    public void updateTimer() {
        secondsAfterStartGame++;
        if (!isAllFieldClose) {
            view.updateScoreBoard(secondsAfterStartGame, currentBombsNumberWithoutMarkedCells);
            game.setCurrentScore(secondsAfterStartGame);
        }
    }
}
