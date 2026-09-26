package it.verde.view;

import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.util.List;

public final class LogisticsManagerView extends BaseView {
    private static final List<String> OPTIONS = List.of(
            "Gestisci Magazzino",
            "Gestisci Ordini di Rifornimento",
            "Gestisci Fornitori",
            "Esci"
    );

    public LogisticsManagerView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    public int showMainMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("Responsabile Logistico", ui.theme().primary(), 80);
        return readMenuChoice("Menù Responsabile Logistico", OPTIONS);
    }
}
