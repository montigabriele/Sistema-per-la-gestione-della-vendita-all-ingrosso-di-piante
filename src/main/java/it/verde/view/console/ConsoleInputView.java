package it.verde.view.console;

import it.verde.view.core.InputView;
import it.verde.view.core.UiTheme;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public final class ConsoleInputView implements InputView {
    private final Scanner scanner;
    private final UiTheme theme;

    public ConsoleInputView(Scanner scanner, UiTheme theme) {
        this.scanner = Objects.requireNonNull(scanner);
        this.theme = Objects.requireNonNull(theme);
    }

    @Override
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    @Override
    public String readRequiredString(String prompt) {
        while (true) {
            String value = readString(prompt);
            if (!value.isEmpty()) return value;
            System.out.println(theme.colorize("Il campo non può essere vuoto. Riprova.", theme.error()));
        }
    }

    @Override
    public int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readString(prompt));
            } catch (NumberFormatException e) {
                System.out.println(theme.colorize("Inserisci un numero valido.", theme.error()));
            }
        }
    }

    @Override
    public int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) return value;
            System.out.println(theme.colorize("Inserisci un numero maggiore di zero.", theme.error()));
        }
    }

    @Override
    public BigDecimal readPositiveDecimal(String prompt) {
        while (true) {
            try {
                BigDecimal value = new BigDecimal(readString(prompt));
                if (value.signum() > 0) return value;
                System.out.println(theme.colorize("Inserisci un numero maggiore di zero.", theme.error()));
            } catch (NumberFormatException e) {
                System.out.println(theme.colorize("Inserisci un numero decimale valido. Usa il punto come separatore decimale.", theme.error()));
            }
        }
    }

    @Override
    public boolean readBoolean(String prompt) {
        return readBoolean(prompt, null);
    }

    @Override
    public boolean readBoolean(String prompt, Boolean defaultValue) {
        while (true) {
            String suffix = "";
            if (defaultValue != null) {
                String defaultLabel = defaultValue ? "sì" : "no";
                suffix = " [Default: " + defaultLabel + "]";
            }
            String value = readString(prompt + suffix + ": ").toLowerCase();
            if (value.isEmpty() && defaultValue != null) return defaultValue;
            if (List.of("sì", "si", "s", "yes", "y").contains(value)) return true;
            if (List.of("no", "n").contains(value)) return false;
            System.out.println(theme.colorize("Risposta non valida. Inserisci 'sì' o 'no'.", theme.error()));
        }
    }

    @Override
    public int readMenuChoice(String title, List<String> options) {
        Objects.requireNonNull(options);
        if (options.isEmpty()) throw new IllegalArgumentException("Menu options cannot be empty");

        System.out.println("\n" + theme.bold(title));
        for (int i = 0; i < options.size(); i++) System.out.printf("%d. %s%n", i + 1, options.get(i));

        while (true) {
            int choice = readInt("Inserisci la tua scelta: ");
            if (choice >= 1 && choice <= options.size()) return choice;
            System.out.println(theme.colorize("Scelta non valida. Seleziona una delle opzioni disponibili.", theme.error()));
        }
    }

    @Override
    public void waitForEnter() {
        System.out.print(theme.colorize("\nPremi INVIO per continuare...", theme.warning()));
        scanner.nextLine();
    }
}
