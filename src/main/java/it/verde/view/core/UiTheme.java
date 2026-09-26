package it.verde.view.core;

public final class UiTheme {
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String PURPLE = "\u001B[35m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String WHITE = "\u001B[37m";

    public String primary() { return BLUE; }
    public String secondary() { return CYAN; }
    public String accent() { return PURPLE; }
    public String error() { return RED; }
    public String warning() { return YELLOW; }
    public String success() { return GREEN; }
    public String info() { return WHITE; }
    public String reset() { return RESET; }
    public String boldCode() { return BOLD; }

    public String colorize(String text, String color) {
        return color + text + RESET;
    }

    public String bold(String text) {
        return BOLD + text + RESET;
    }
}
