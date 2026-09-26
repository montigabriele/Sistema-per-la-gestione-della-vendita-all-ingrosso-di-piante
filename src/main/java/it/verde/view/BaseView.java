package it.verde.view;

import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public abstract class BaseView {
    protected final InputView input;
    protected final OutputView output;
    protected final UiManager ui;

    protected BaseView(InputView input, OutputView output, UiManager ui) {
        this.input = Objects.requireNonNull(input);
        this.output = Objects.requireNonNull(output);
        this.ui = Objects.requireNonNull(ui);
    }

    protected String readString(String prompt) { return input.readString(prompt); }
    protected String readRequiredString(String prompt) { return input.readRequiredString(prompt); }
    protected int readInt(String prompt) { return input.readInt(prompt); }
    protected int readPositiveInt(String prompt) { return input.readPositiveInt(prompt); }
    protected BigDecimal readPositiveDecimal(String prompt) { return input.readPositiveDecimal(prompt); }
    protected boolean readBoolean(String prompt) { return input.readBoolean(prompt); }
    protected boolean readBoolean(String prompt, Boolean defaultValue) { return input.readBoolean(prompt, defaultValue); }
    protected int readMenuChoice(String title, List<String> options) { return input.readMenuChoice(title, options); }
    public void waitForEnter() { input.waitForEnter(); }
    public void showMessage(String message) { output.showMessage(message); }
    public void showError(String message) { output.showError(message); }
    public void showWarning(String message) { output.showWarning(message); }
    public void showSuccess(String message) { output.showSuccess(message); }
    protected void showTitle(String title, String color, int width) { output.showTitle(title, color, width); }
    protected void showTable(List<String> headers, List<List<String>> rows, List<Integer> widths) { output.showTable(headers, rows, widths); }
    protected void showSeparator() { output.showSeparator(); }
    public void clearScreen() { ui.clearScreen(); }
}
