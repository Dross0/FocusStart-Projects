package ru.gaidamaka.ui;

import ru.gaidamaka.UserEvent;
import ru.gaidamaka.highscoretable.HighScoreTable;
import ru.gaidamaka.presenter.Presenter;
import ru.gaidamaka.game.Cell;


public interface View {
    void drawCell(Cell cell);

    void updateScoreBoard(int score, int flagsNumber);

    void fireEvent(UserEvent event);

    void setPresenter(Presenter presenter);

    void showLoseScreen();

    void showWinScreen();

    void showHighScoreTable(HighScoreTable table);

    String readPlayerName();
}
