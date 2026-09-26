package it.verde.view.console;

import it.verde.view.core.UiManager;
import it.verde.view.core.UiTheme;

import java.util.Objects;

public record ConsoleUiManager(UiTheme theme) implements UiManager {
    public ConsoleUiManager {
        Objects.requireNonNull(theme);
    }

    @Override
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void showHeader() {
        System.out.print(theme.boldCode() + theme.success() +
                "╔════════════════════════════════════════════════════════════════════════════════╗\n" +
                "║                              SISTEMA VERDE S.r.l.                              ║\n" +
                "║                   Gestione vendita all'ingrosso di piante                      ║\n" +
                "╚════════════════════════════════════════════════════════════════════════════════╝\n" +
                theme.reset());
    }
}
