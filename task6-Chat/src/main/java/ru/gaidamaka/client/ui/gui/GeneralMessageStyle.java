package ru.gaidamaka.client.ui.gui;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.Objects;

public class GeneralMessageStyle {
    private static final Color DATE_COLOR = Color.BLUE;
    private static final Color USER_NAME_COLOR = Color.GREEN;
    private static final Color CONTENT_COLOR = Color.BLACK;

    @NotNull
    private final Style dateStyle;
    @NotNull
    private final Style userNameStyle;
    @NotNull
    private final Style contentStyle;


    public GeneralMessageStyle(@NotNull JTextPane messagesTextPane) {
        Objects.requireNonNull(messagesTextPane);
        dateStyle = messagesTextPane.addStyle("dateStyle", null);
        userNameStyle = messagesTextPane.addStyle("userNameStyle", null);
        contentStyle = messagesTextPane.addStyle("contentStyle", null);
        setColors();
    }

    private void setColors() {
        StyleConstants.setForeground(dateStyle, DATE_COLOR);
        StyleConstants.setForeground(contentStyle, CONTENT_COLOR);
        StyleConstants.setForeground(userNameStyle, USER_NAME_COLOR);
    }

    @NotNull
    public Style getDateStyle() {
        return dateStyle;
    }

    @NotNull
    public Style getContentStyle() {
        return contentStyle;
    }

    @NotNull
    public Style getUserNameStyle() {
        return userNameStyle;
    }
}
