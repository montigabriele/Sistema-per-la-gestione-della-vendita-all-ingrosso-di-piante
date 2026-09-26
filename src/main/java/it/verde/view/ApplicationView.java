package it.verde.view;

import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

public final class ApplicationView extends BaseView {
    public ApplicationView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    public void showApplicationHeader() {
        clearScreen();
        ui.showHeader();
    }
}
