package it.verde.view.console;

import it.verde.view.core.OutputView;
import it.verde.view.core.UiTheme;

import java.util.List;
import java.util.Objects;

public final class ConsoleOutputView implements OutputView {
    private final UiTheme theme;

    public ConsoleOutputView(UiTheme theme) {
        this.theme = Objects.requireNonNull(theme);
    }

    @Override
    public void showMessage(String message) {
        System.out.println(theme.colorize(message, theme.info()));
    }

    @Override
    public void showError(String message) {
        System.out.println(theme.colorize(message, theme.error()));
    }

    @Override
    public void showWarning(String message) {
        System.out.println(theme.colorize(message, theme.warning()));
    }

    @Override
    public void showSuccess(String message) {
        System.out.println(theme.colorize(message, theme.success()));
    }

    @Override
    public void showTitle(String title, String color, int width) {
        String text = title == null ? "" : title.toUpperCase();
        int innerWidth = Math.max(width, text.length() + 2);
        int leftPadding = Math.max((innerWidth - text.length()) / 2, 0);
        int rightPadding = Math.max(innerWidth - text.length() - leftPadding, 0);
        String top = "╔" + "═".repeat(innerWidth) + "╗";
        String middle = "║" + " ".repeat(leftPadding) + text + " ".repeat(rightPadding) + "║";
        String bottom = "╚" + "═".repeat(innerWidth) + "╝";
        System.out.println(color + top + "\n" + middle + "\n" + bottom + theme.reset());
    }

    @Override
    public void showTable(List<String> headers, List<List<String>> rows, List<Integer> columnWidths) {
        Objects.requireNonNull(headers);
        Objects.requireNonNull(rows);
        Objects.requireNonNull(columnWidths);
        if (headers.size() != columnWidths.size()) throw new IllegalArgumentException("Headers and column widths must have the same size");

        String border = buildBorder(columnWidths);
        System.out.println(theme.colorize(border, theme.info()));
        printRow(headers, columnWidths);
        System.out.println(theme.colorize(border, theme.info()));
        for (List<String> row : rows) {
            if (row.size() != headers.size()) throw new IllegalArgumentException("Table row has an invalid number of columns");
            printRow(row, columnWidths);
        }
        System.out.println(theme.colorize(border, theme.info()));
    }

    @Override
    public void showSeparator() {
        System.out.println(theme.colorize("═".repeat(80), theme.info()));
    }

    private String buildBorder(List<Integer> widths) {
        int width = widths.stream().mapToInt(Integer::intValue).sum() + (widths.size() * 3) - 1;
        return "═".repeat(width);
    }

    private void printRow(List<String> values, List<Integer> widths) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) line.append(" | ");
            line.append(pad(truncate(values.get(i), widths.get(i)), widths.get(i)));
        }
        System.out.println(line);
    }

    private String truncate(String value, int width) {
        String text = value == null ? "" : value;
        if (text.length() <= width) return text;
        if (width <= 1) return text.substring(0, width);
        return text.substring(0, width - 1) + "…";
    }

    private String pad(String value, int width) {
        return value + " ".repeat(Math.max(width - value.length(), 0));
    }
}
