package ru.gaidamaka.ui;

import org.jetbrains.annotations.NotNull;
import ru.gaidamaka.game.cell.Cell;
import ru.gaidamaka.highscoretable.HighScoreTable;
import ru.gaidamaka.presenter.Presenter;
import ru.gaidamaka.userevent.UserEvent;


public interface View {
    void drawCell(@NotNull Cell cell);

    void updateFlagsNumber(int flagsNumber);

    void fireEvent(@NotNull UserEvent event);

    void setPresenter(@NotNull Presenter presenter);

    void showLoseScreen();

    void showWinScreen();

    void showHighScoreTable(@NotNull HighScoreTable table);

    @NotNull
    String readPlayerName();

    void showErrorMessage(@NotNull String errorMessage);

    void showAbout(@NotNull String aboutText);

    void updateScore(int score);
}
