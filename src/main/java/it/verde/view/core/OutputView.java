package it.verde.view.core;

import java.util.List;

public interface OutputView {
    void showMessage(String message);
    void showError(String message);
    void showWarning(String message);
    void showSuccess(String message);
    void showTitle(String title, String color, int width);
    void showTable(List<String> headers, List<List<String>> rows, List<Integer> columnWidths);
    void showSeparator();
}
