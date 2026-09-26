package it.verde.view;

import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.util.List;

public final class AdministratorView extends BaseView {
    private static final List<String> OPTIONS = List.of("Gestisci Utenti", "Esci");

    public AdministratorView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    public int showMainMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("Amministratore", ui.theme().error(), 80);
        return readMenuChoice("Menù Amministratore", OPTIONS);
    }
}
