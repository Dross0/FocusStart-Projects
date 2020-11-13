package ru.gaidamaka.presenter;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gaidamaka.exception.HighScoreTableManagerException;
import ru.gaidamaka.game.Game;
import ru.gaidamaka.game.event.GameEvent;
import ru.gaidamaka.highscoretable.HighScoreTableManager;
import ru.gaidamaka.highscoretable.PlayerRecord;
import ru.gaidamaka.timer.Timer;
import ru.gaidamaka.ui.View;
import ru.gaidamaka.userevent.*;

import java.util.Objects;

public class MinesweeperPresenter implements Presenter {
    private static final Logger logger = LoggerFactory.getLogger(MinesweeperPresenter.class);

    private final View view;
    private final Game game;
    private final Timer timer;
    private final HighScoreTableManager highScoreTableManager;
    private int secondsAfterStartGame;

    public MinesweeperPresenter(@NotNull Game game,
                                @NotNull View view,
                                @NotNull HighScoreTableManager manager){
        this.game = Objects.requireNonNull(game, "Game cant be null");
        this.view = Objects.requireNonNull(view, "View cant be null");
        this.highScoreTableManager = Objects.requireNonNull(manager, "High score table manager cant be null");
        this.timer = new Timer();
        timer.addObserver(this);
        game.addObserver(this);
        view.setPresenter(this);
    }

    @Override
    public void onEvent(@NotNull UserEvent event) {
        Objects.requireNonNull(event, "User event cant be null");
        switch (event.getType()) {
            case EXIT_GAME:
                game.exit();
                timer.stop();
                break;
            case FLAG_SET:
                FlagSetEvent flagSetEvent = (FlagSetEvent) event;
                game.toggleMarkCell(flagSetEvent.getX(), flagSetEvent.getY());
                view.updateScoreBoard(secondsAfterStartGame, game.getCurrentBombsNumberWithoutMarkedCells());
                break;
            case SHOW_CELL:
                ShowCellEvent showCellEvent = (ShowCellEvent) event;
                game.showCell(showCellEvent.getX(), showCellEvent.getY());
                break;
            case SHOW_NEAR_EMPTY_CELLS:
                ShowNearEmptyCellsEvent nearEmptyCellsEvent = (ShowNearEmptyCellsEvent) event;
                game.showNeighborsOfOpenCell(nearEmptyCellsEvent.getX(), nearEmptyCellsEvent.getY());
                break;
            case NEW_GAME:
                NewGameEvent newGameEvent = (NewGameEvent) event;
                game.reset(
                        newGameEvent.getFieldWidth(),
                        newGameEvent.getFieldHeight(),
                        newGameEvent.getBombsNumber()
                );
                runGame();
                break;
            case SHOW_HIGH_SCORE_TABLE:
                view.showHighScoreTable(highScoreTableManager.getOrCreateTable());
                break;
        }
    }

    public void runGame(){
        secondsAfterStartGame = 0;
        updateGameHighScoreTable();
        game.run();
    }

    private void showResultWindow(GameEvent gameEvent){
        switch (gameEvent.getCurrentGameStatus()){
            case WIN:
                if (gameEvent.isNewHighScore()) {
                    String playerName = view.readPlayerName();
                    highScoreTableManager
                            .getOrCreateTable()
                            .addNewRecord(new PlayerRecord(playerName, gameEvent.getScore()));
                }
                view.showWinScreen();
                try {
                    highScoreTableManager.save();
                } catch (HighScoreTableManagerException e) {
                    logger.warn("Cant save high score table", e);
                }
                break;
            case LOSE:
                view.showLoseScreen();
                break;
        }
    }


    @Override
    public void update(@NotNull GameEvent gameEvent) {
        Objects.requireNonNull(gameEvent, "Game event cant be null");
        switch (gameEvent.getEventType()){
            case START_GAME:
                timer.start();
                break;
            case FINISH_GAME:
                showResultWindow(gameEvent);
                break;
        }
        gameEvent.getUpdatedCells().forEach(view::drawCell);

    }

    private void updateGameHighScoreTable() {
        try {
            highScoreTableManager.read();
        } catch (HighScoreTableManagerException e) {
            logger.warn("Cant read high score table, default table will be used", e);
        }
        game.setHighScoreTable(highScoreTableManager.getOrCreateTable());
    }

    @Override
    public void updateTimer() {
        secondsAfterStartGame++;
        view.updateScoreBoard(secondsAfterStartGame, game.getCurrentBombsNumberWithoutMarkedCells());
        game.setCurrentScore(secondsAfterStartGame);
    }
}