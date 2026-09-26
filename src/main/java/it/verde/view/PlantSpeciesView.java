package it.verde.view;

import it.verde.view.bean.PlantSpeciesBean;
import it.verde.view.bean.SpeciesCatalogItemBean;
import it.verde.view.bean.SpeciesSalesReportBean;
import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public final class PlantSpeciesView extends BaseView {
    private static final List<String> MAIN_OPTIONS = List.of(
            "Inserisci Specie",
            "Visualizza Elenco Specie",
            "Visualizza Catalogo con prezzi e giacenze",
            "Visualizza Report Vendite",
            "Modifica Dati Specie",
            "Elimina Specie",
            "Ricerca avanzata",
            "Torna al Menù Principale"
    );

    private static final List<String> SEARCH_OPTIONS = List.of(
            "Cerca per Nome (Comune o Latino)",
            "Cerca per Tipologia",
            "Cerca per Colorazione",
            "Torna al Menù Specie"
    );

    private static final String HEADER_CODE = "CODICE";
    private static final String HEADER_COMMON_NAME = "NOME COMUNE";
    private static final String HEADER_PLANT_TYPE = "TIPOLOGIA";

    private static final List<String> PLANT_TYPE_LABELS = List.of("APPARTAMENTO", "GIARDINO");
    private static final List<String> PLANT_TYPE_VALUES = List.of("INDOOR", "GARDEN");
    private static final List<String> FLOWER_COLOR_LABELS = List.of("ROSSA", "GIALLA", "ARANCIONE", "BLU", "VIOLA", "BIANCA", "MULTICOLORE");
    private static final List<String> FLOWER_COLOR_VALUES = List.of("RED", "YELLOW", "ORANGE", "BLUE", "PURPLE", "WHITE", "MULTICOLOR");

    public PlantSpeciesView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    public int showMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("Gestione Specie di Piante", ui.theme().info(), 80);
        return readMenuChoice("Gestione Specie di Piante", MAIN_OPTIONS);
    }

    public int showSearchMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("Ricerca Specie di Piante", ui.theme().secondary(), 80);
        return readMenuChoice("Seleziona un tipo di ricerca", SEARCH_OPTIONS);
    }

    public PlantSpeciesBean readNewSpecies() {
        clearScreen();
        ui.showHeader();
        showTitle("Inserimento Specie di Piante", ui.theme().success(), 80);

        PlantSpeciesBean bean = new PlantSpeciesBean();
        bean.setCommonName(readRequiredString("Nome comune: "));
        bean.setLatinName(readRequiredString("Nome latino: "));
        bean.setPlantType(readPlantType());
        bean.setExotic(readBoolean("È esotica"));
        boolean flowering = readBoolean("È fiorita");
        bean.setFlowering(flowering);
        bean.setFlowerColor(flowering ? readFlowerColor() : null);
        return bean;
    }

    public BigDecimal readInitialPrice() {
        return readPositiveDecimal("Prezzo iniziale (€): ");
    }

    public String readSpeciesCode() {
        return readRequiredString("Inserisci il codice della specie: ").toUpperCase();
    }

    public PlantSpeciesBean readUpdatedSpecies(PlantSpeciesBean current) {
        if (current == null) throw new IllegalArgumentException("Current plant species cannot be null");

        clearScreen();
        ui.showHeader();
        showTitle("Modifica Specie di Piante", ui.theme().warning(), 80);
        showSpeciesSummary(current);

        PlantSpeciesBean updated = new PlantSpeciesBean();
        updated.setSpeciesCode(current.getSpeciesCode());
        updated.setCommonName(readKeepingCurrent("Nome comune", current.getCommonName()));
        updated.setLatinName(readKeepingCurrent("Nome latino", current.getLatinName()));
        updated.setPlantType(current.getPlantType());
        updated.setExotic(readBoolean("È esotica", current.getExotic()));

        boolean flowering = readBoolean("È fiorita", current.getFlowering());
        updated.setFlowering(flowering);
        if (flowering) {
            updated.setFlowerColor(Boolean.TRUE.equals(current.getFlowering()) && current.getFlowerColor() != null
                    ? readFlowerColorKeepingCurrent(current.getFlowerColor())
                    : readFlowerColor());
        } else {
            updated.setFlowerColor(null);
        }
        return updated;
    }

    public String readSearchTerm() {
        return readRequiredString("Inserisci il termine di ricerca: ");
    }

    public String readPlantType() {
        int choice = readMenuChoice("Tipologia", PLANT_TYPE_LABELS);
        return PLANT_TYPE_VALUES.get(choice - 1);
    }

    public String readFlowerColor() {
        int choice = readMenuChoice("Seleziona la colorazione", FLOWER_COLOR_LABELS);
        return FLOWER_COLOR_VALUES.get(choice - 1);
    }

    public void showSpeciesList(List<PlantSpeciesBean> species) {
        clearScreen();
        ui.showHeader();
        showTitle("Elenco specie di piante", ui.theme().primary(), 122);

        if (species == null || species.isEmpty()) {
            showMessage("Nessuna specie registrata.");
            waitForEnter();
            return;
        }

        List<String> headers = List.of(HEADER_CODE, HEADER_COMMON_NAME, "NOME LATINO", HEADER_PLANT_TYPE, "ESOTICA", "COLORAZIONE");
        List<Integer> widths = List.of(12, 28, 28, 15, 10, 14);
        List<List<String>> rows = new ArrayList<>();

        for (PlantSpeciesBean item : species) {
            rows.add(List.of(
                    text(item.getSpeciesCode()),
                    text(item.getCommonName()),
                    text(item.getLatinName()),
                    plantTypeLabel(item.getPlantType()),
                    yesNo(item.getExotic()),
                    Boolean.TRUE.equals(item.getFlowering()) ? flowerColorLabel(item.getFlowerColor()) : "-"
            ));
        }

        showTable(headers, rows, widths);
        waitForEnter();
    }

    public void showCatalog(List<SpeciesCatalogItemBean> catalog) {
        clearScreen();
        ui.showHeader();
        showTitle("Catalogo Specie di Piante", ui.theme().primary(), 148);

        if (catalog == null || catalog.isEmpty()) {
            showMessage("Nessuna specie disponibile al momento.");
            waitForEnter();
            return;
        }

        List<String> headers = List.of(HEADER_CODE, HEADER_COMMON_NAME, "NOME LATINO", HEADER_PLANT_TYPE, "ESOTICA", "COLORAZIONE", "PREZZO (€)", "GIACENZA");
        List<Integer> widths = List.of(12, 26, 26, 15, 10, 14, 12, 10);
        List<List<String>> rows = new ArrayList<>();

        for (SpeciesCatalogItemBean item : catalog) {
            rows.add(List.of(
                    text(item.getSpeciesCode()),
                    text(item.getCommonName()),
                    text(item.getLatinName()),
                    plantTypeLabel(item.getPlantType()),
                    yesNo(item.getExotic()),
                    item.getFlowerColor() == null ? "-" : flowerColorLabel(item.getFlowerColor()),
                    formatMoney(item.getCurrentPrice()),
                    item.getStockQuantity() == null ? "0" : item.getStockQuantity().toString()
            ));
        }

        showTable(headers, rows, widths);
        waitForEnter();
    }

    public void showSalesReport(List<SpeciesSalesReportBean> report) {
        clearScreen();
        ui.showHeader();
        showTitle("Report vendite", ui.theme().success(), 112);

        if (report == null || report.isEmpty()) {
            showMessage("Nessuna vendita registrata.");
            waitForEnter();
            return;
        }

        List<String> headers = List.of(HEADER_CODE, HEADER_COMMON_NAME, HEADER_PLANT_TYPE, "FIORITA", "Q. VENDUTA", "VALORE TOTALE (€)");
        List<Integer> widths = List.of(12, 28, 15, 10, 12, 18);
        List<List<String>> rows = new ArrayList<>();

        for (SpeciesSalesReportBean item : report) {
            rows.add(List.of(
                    text(item.getSpeciesCode()),
                    text(item.getCommonName()),
                    plantTypeLabel(item.getPlantType()),
                    yesNo(item.getFlowering()),
                    item.getSoldQuantity() == null ? "0" : item.getSoldQuantity().toString(),
                    formatMoney(item.getTotalSalesValue())
            ));
        }

        showTable(headers, rows, widths);
        waitForEnter();
    }

    public void showCreateSuccess(String speciesCode) {
        clearScreen();
        ui.showHeader();
        showSuccess("Specie '" + speciesCode + "' inserita con successo!");
    }

    public void showUpdateSuccess() {
        showSuccess("Specie modificata con successo.");
    }

    public void showDeleteSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Specie rimossa con successo.");
    }

    public void showNoSearchResults(String description) {
        showMessage("Nessuna specie trovata" + (description == null || description.isBlank() ? "." : " " + description + "."));
        waitForEnter();
    }

    private String readKeepingCurrent(String label, String currentValue) {
        String value = readString(String.format("%s [%s] (INVIO per mantenere): ", label, text(currentValue)));
        return value.isEmpty() ? currentValue : value;
    }

    private String readFlowerColorKeepingCurrent(String currentColor) {
        List<String> options = new ArrayList<>();
        options.add("Mantieni " + flowerColorLabel(currentColor));
        options.addAll(FLOWER_COLOR_LABELS);
        int choice = readMenuChoice("Seleziona la nuova colorazione", options);
        return choice == 1 ? currentColor : FLOWER_COLOR_VALUES.get(choice - 2);
    }

    private void showSpeciesSummary(PlantSpeciesBean species) {
        showMessage("Codice: " + text(species.getSpeciesCode()));
        showMessage("Tipologia: " + plantTypeLabel(species.getPlantType()));
        showMessage("Colorazione: " + (Boolean.TRUE.equals(species.getFlowering()) ? flowerColorLabel(species.getFlowerColor()) : "-"));
        showSeparator();
    }

    private String plantTypeLabel(String value) {
        if (value == null) return "-";
        return switch (value.toUpperCase()) {
            case "INDOOR" -> "APPARTAMENTO";
            case "GARDEN" -> "GIARDINO";
            default -> value;
        };
    }

    private String flowerColorLabel(String value) {
        if (value == null) return "-";
        return switch (value.toUpperCase()) {
            case "RED" -> "ROSSA";
            case "YELLOW" -> "GIALLA";
            case "ORANGE" -> "ARANCIONE";
            case "BLUE" -> "BLU";
            case "PURPLE" -> "VIOLA";
            case "WHITE" -> "BIANCA";
            case "MULTICOLOR" -> "MULTICOLORE";
            default -> value;
        };
    }

    private String yesNo(Boolean value) {
        if (value == null) return "-";
        return value ? "SI" : "NO";
    }

    private String formatMoney(BigDecimal value) {
        return value == null ? "0.00" : value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String text(String value) {
        return value == null ? "" : value;
    }
}
