package it.verde.view;

import it.verde.view.bean.PriceHistoryBean;
import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class PriceView extends BaseView {
    private static final List<String> OPTIONS = List.of(
            "Inserisci variazione prezzo",
            "Visualizza storico prezzi",
            "Torna al Menù Principale"
    );

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSSSSS", Locale.ITALIAN);

    public PriceView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    public int showMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("Gestione Prezzi", ui.theme().primary(), 80);
        return readMenuChoice("Gestione catalogo prezzi", OPTIONS);
    }

    public String readSpeciesCode() {
        return readRequiredString("Inserisci il codice della specie: ").toUpperCase();
    }

    public BigDecimal readNewPrice() {
        return readPositiveDecimal("Inserisci il nuovo prezzo (€): ");
    }

    public void showPriceHistory(List<PriceHistoryBean> history) {
        clearScreen();
        ui.showHeader();
        showTitle("Storico Prezzi", ui.theme().info(), 72);

        if (history == null || history.isEmpty()) {
            showMessage("Nessuno storico trovato per la specie indicata.");
            waitForEnter();
            return;
        }

        String speciesCode = history.stream()
                .map(PriceHistoryBean::getSpeciesCode)
                .filter(code -> code != null && !code.isBlank())
                .findFirst()
                .orElse(null);

        if (speciesCode != null) {
            showMessage("Specie: " + speciesCode);
            showSeparator();
        }

        List<String> headers = List.of("DATA VARIAZIONE", "PREZZO PRECEDENTE (€)", "PREZZO ATTUALE (€)");
        List<Integer> widths = List.of(28, 24, 21);
        List<List<String>> rows = new ArrayList<>();

        for (PriceHistoryBean item : history) {
            rows.add(List.of(
                    item.getChangedAt() == null ? "-" : item.getChangedAt().format(DATE_FORMATTER),
                    item.getPreviousPrice() == null ? "-" : formatMoney(item.getPreviousPrice()),
                    item.getCurrentPrice() == null ? "-" : formatMoney(item.getCurrentPrice())
            ));
        }

        showTable(headers, rows, widths);
        waitForEnter();
    }

    public void showPriceChangeSuccess(String speciesCode, BigDecimal newPrice) {
        clearScreen();
        ui.showHeader();
        String code = speciesCode == null || speciesCode.isBlank() ? "" : " per " + speciesCode.toUpperCase();
        String price = newPrice == null ? "" : " Nuovo prezzo: € " + formatMoney(newPrice) + ".";
        showSuccess("Prezzo modificato con successo" + code + "." + price);
    }

    private String formatMoney(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
