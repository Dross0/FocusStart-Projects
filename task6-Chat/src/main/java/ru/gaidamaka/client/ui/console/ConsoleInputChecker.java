package ru.gaidamaka.client.ui.console;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleInputChecker {
    @NotNull
    private final ConsoleMessageSender sender;

    @NotNull
    private final Thread inputCheckerThread;

    ConsoleInputChecker(@NotNull ConsoleMessageSender sender) {
        this.sender = Objects.requireNonNull(sender, "Sender cant be null");
        this.inputCheckerThread = new Thread(getInputCheckRunnable());
    }

    void startInputCheck() {
        inputCheckerThread.start();
    }

    void stopInputCheck() {
        inputCheckerThread.interrupt();
    }

    @NotNull
    private Runnable getInputCheckRunnable() {
        return () -> {
            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
            while (!Thread.currentThread().isInterrupted()) {
                if (!scanner.hasNextLine()) {
                    continue;
                }
                String rawMessage = scanner.nextLine();
                sender.send(rawMessage);
            }
        };
    }
}
