package it.verde.view;

import it.verde.view.bean.CriticalStockBean;
import it.verde.view.bean.SpeciesCatalogItemBean;
import it.verde.view.bean.WarehouseStockBean;
import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.util.ArrayList;
import java.util.List;

public final class WarehouseView extends BaseView {
    private static final List<String> OPTIONS = List.of(
            "Inserisci Giacenza",
            "Visualizza Giacenze",
            "Visualizza Giacenze Critiche",
            "Modifica Giacenza",
            "Torna al Menù Principale"
    );

    public WarehouseView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    public int showMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("Magazzino", ui.theme().info(), 80);
        return readMenuChoice("Gestione Magazzino", OPTIONS);
    }

    public WarehouseStockBean readNewStock() {
        clearScreen();
        ui.showHeader();
        showTitle("Inserimento Giacenza", ui.theme().success(), 80);

        WarehouseStockBean bean = new WarehouseStockBean();
        bean.setSpeciesCode(readSpeciesCode());
        bean.setQuantity(readQuantity());
        return bean;
    }

    public String readSpeciesCode() {
        while (true) {
            String value = readRequiredString("Inserisci il codice della specie: ").trim().toUpperCase();
            WarehouseStockBean probe = new WarehouseStockBean();
            try {
                probe.setSpeciesCode(value);
                return probe.getSpeciesCode();
            } catch (IllegalArgumentException e) {
                showError("Codice specie non valido. Formato atteso: SP-F000-IN oppure SP-N000-ES.");
            }
        }
    }

    public int readQuantity() {
        while (true) {
            int quantity = readInt("Inserisci la quantità (0 o maggiore): ");
            if (quantity >= 0) return quantity;
            showError("La quantità non può essere negativa.");
        }
    }

    public WarehouseStockBean readUpdatedStock(String speciesCode, int currentQuantity) {
        clearScreen();
        ui.showHeader();
        showTitle("Modifica Giacenza", ui.theme().warning(), 80);
        showMessage("Specie: " + speciesCode);
        showMessage("Giacenza attuale: " + currentQuantity);
        showSeparator();

        WarehouseStockBean bean = new WarehouseStockBean();
        bean.setSpeciesCode(speciesCode);
        bean.setQuantity(readQuantity());
        return bean;
    }

    public void showWarehouseOverview(List<SpeciesCatalogItemBean> stocks) {
        clearScreen();
        ui.showHeader();
        showTitle("Elenco Giacenze Disponibili", ui.theme().primary(), 62);

        if (stocks == null || stocks.isEmpty()) {
            showMessage("Nessuna giacenza trovata.");
            waitForEnter();
            return;
        }

        List<String> headers = List.of("CODICE", "NOME COMUNE", "GIACENZA");
        List<Integer> widths = List.of(14, 32, 10);
        List<List<String>> rows = new ArrayList<>();

        for (SpeciesCatalogItemBean item : stocks) {
            rows.add(List.of(
                    text(item.getSpeciesCode()),
                    text(item.getCommonName()),
                    item.getStockQuantity() == null ? "0" : item.getStockQuantity().toString()
            ));
        }

        showTable(headers, rows, widths);
        waitForEnter();
    }

    public void showCriticalStocks(List<CriticalStockBean> stocks) {
        clearScreen();
        ui.showHeader();
        showTitle("Giacenze Critiche (< 10)", ui.theme().error(), 94);

        if (stocks == null || stocks.isEmpty()) {
            showMessage("Nessuna giacenza critica trovata.");
            waitForEnter();
            return;
        }

        List<String> headers = List.of("CODICE", "NOME COMUNE", "NOME LATINO", "QUANTITÀ");
        List<Integer> widths = List.of(14, 30, 32, 10);
        List<List<String>> rows = new ArrayList<>();

        for (CriticalStockBean item : stocks) {
            rows.add(List.of(
                    text(item.getSpeciesCode()),
                    text(item.getCommonName()),
                    text(item.getLatinName()),
                    item.getQuantity() == null ? "0" : item.getQuantity().toString()
            ));
        }

        showTable(headers, rows, widths);
        waitForEnter();
    }

    public void showCreateSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Giacenza inserita correttamente.");
    }

    public void showUpdateSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Giacenza modificata con successo.");
    }

    private String text(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
