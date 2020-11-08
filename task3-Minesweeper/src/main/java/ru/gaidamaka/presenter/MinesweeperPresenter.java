package ru.gaidamaka.presenter;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.UserEvent;
import ru.gaidamaka.exception.HighScoreTableManagerException;
import ru.gaidamaka.game.Game;
import ru.gaidamaka.game.event.GameEvent;
import ru.gaidamaka.highscoretable.HighScoreTable;
import ru.gaidamaka.highscoretable.HighScoreTableManager;
import ru.gaidamaka.highscoretable.PlayerRecord;
import ru.gaidamaka.timer.Timer;
import ru.gaidamaka.ui.View;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinesweeperPresenter implements Presenter {
    private static final Logger logger = Logger.getLogger(MinesweeperPresenter.class.getName());

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
        switch (event.getType()){
            case FLAG_SET:
                game.toggleMarkCell(event.getX(), event.getY());
                view.updateScoreBoard(secondsAfterStartGame, game.getCurrentBombsNumberWithoutMarkedCells());
                break;
            case SHOW_CELL:
                game.showCell(event.getX(), event.getY());
                break;
            case SHOW_NEAR_EMPTY_CELLS:
                game.showNeighborsOfOpenCell(event.getX(), event.getY());
                break;
            case NEW_GAME:
                game.reset(10, 10, 10); //FIXME
                runGame();
                break;
            case SHOW_HIGH_SCORE_TABLE:
                String name = view.readPlayerName();
                System.out.println("Name = " + name);
                HighScoreTable table = highScoreTableManager.getOrCreateTable();
                table.addNewRecord(new PlayerRecord("Ali", 100));
                table.addNewRecord(new PlayerRecord("Kris", 300));
                table.addNewRecord(new PlayerRecord("Dross", 400));
                table.addNewRecord(new PlayerRecord("Andrew", 160));
                table.addNewRecord(new PlayerRecord("Hel", 200));

                //view.showHighScoreTable(table);
                break;
        }
    }

    public void runGame(){
        secondsAfterStartGame = 0;
        game.run();
    }

    private void showResultWindow(GameEvent gameEvent){
        switch (gameEvent.getCurrentGameStatus()){
            case WIN:
                if (isNewHighScore(gameEvent.getScore())){
                    String playerName = view.readPlayerName();
                    highScoreTableManager
                            .getOrCreateTable()
                            .addNewRecord(new PlayerRecord(playerName, gameEvent.getScore()));

                }
                view.showWinScreen();
                try {
                    highScoreTableManager.save();
                } catch (HighScoreTableManagerException e) {
                    logger.log(Level.WARNING,"Cant save high score table", e);
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

    private boolean isNewHighScore(int score) {
        try {
            highScoreTableManager.read();
        } catch (HighScoreTableManagerException e) {
            logger.log(Level.WARNING, "Cant read high score table, default table will be used", e);
        }
        HighScoreTable table = highScoreTableManager.getOrCreateTable();
        return table.isHighScore(score);
    }

    @Override
    public void updateTimer() {
        secondsAfterStartGame++;
        view.updateScoreBoard(secondsAfterStartGame, game.getCurrentBombsNumberWithoutMarkedCells());
        game.setCurrentScore(secondsAfterStartGame);
    }
}
