package it.verde.view;

import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.util.List;

public final class SalesManagerView extends BaseView {
    private static final List<String> OPTIONS = List.of(
            "Gestisci Specie di Piante",
            "Gestisci Catalogo Prezzi",
            "Gestisci Ordini di Vendita",
            "Gestisci Aziende Clienti",
            "Esci"
    );

    public SalesManagerView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    public int showMainMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("Responsabile Commerciale", ui.theme().warning(), 80);
        return readMenuChoice("Menù Responsabile Commerciale", OPTIONS);
    }
}
