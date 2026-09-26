package it.verde.view;

import it.verde.view.bean.AddressBean;
import it.verde.view.bean.PlantSpeciesBean;
import it.verde.view.bean.SupplierBean;
import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.util.ArrayList;
import java.util.List;

public final class SupplierView extends BaseView {
    private static final String BACK_TO_PREVIOUS_MENU = "Torna al Menù precedente";
    private static final List<String> MAIN_OPTIONS = List.of(
            "Inserisci Fornitore",
            "Visualizza Fornitori",
            "Modifica Fornitore",
            "Elimina Fornitore",
            "Gestisci Forniture",
            "Torna al Menù Principale"
    );

    private static final List<String> VIEW_OPTIONS = List.of(
            "Visualizza tutti i fornitori",
            "Visualizza fornitore specifico",
            BACK_TO_PREVIOUS_MENU
    );

    private static final List<String> UPDATE_OPTIONS = List.of(
            "Modifica dati fornitore",
            "Modifica indirizzi fornitore",
            BACK_TO_PREVIOUS_MENU
    );

    private static final List<String> SUPPLY_OPTIONS = List.of(
            "Inserisci fornitura specie",
            "Elimina fornitura specie",
            "Visualizza specie fornite",
            "Verifica se il fornitore fornisce una specie",
            BACK_TO_PREVIOUS_MENU
    );

    private static final List<String> ADDRESS_TYPE_LABELS = List.of("LEGALE", "FATTURAZIONE");
    private static final List<String> ADDRESS_TYPE_VALUES = List.of("LEGAL", "BILLING");

    public SupplierView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    public int showMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("GESTIONE FORNITORI", ui.theme().primary(), 80);
        return readMenuChoice("Gestione Fornitori", MAIN_OPTIONS);
    }

    public int showViewMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("VISUALIZZA FORNITORI", ui.theme().secondary(), 72);
        return readMenuChoice("Opzioni Visualizzazione", VIEW_OPTIONS);
    }

    public int showUpdateMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("MODIFICA FORNITORI", ui.theme().warning(), 72);
        return readMenuChoice("Opzioni Modifica", UPDATE_OPTIONS);
    }

    public int showSupplyMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("GESTIONE FORNITURE", ui.theme().success(), 72);
        return readMenuChoice("Gestione Forniture", SUPPLY_OPTIONS);
    }

    public SupplierBean readNewSupplier() {
        clearScreen();
        ui.showHeader();
        showTitle("INSERIMENTO NUOVO FORNITORE", ui.theme().success(), 80);

        SupplierBean bean = new SupplierBean();
        bean.setName(readRequiredString("Nome fornitore: ").trim());
        bean.setTaxCode(readTaxCode());
        bean.setAddresses(readAddresses());
        return bean;
    }

    public SupplierBean readUpdatedSupplier(SupplierBean current) {
        if (current == null) throw new IllegalArgumentException("Current supplier cannot be null");

        clearScreen();
        ui.showHeader();
        showTitle("MODIFICA FORNITORE #" + text(current.getSupplierId()), ui.theme().warning(), 80);
        showSupplierDetails(current, false);
        showMessage("Lascia vuoto per mantenere il valore attuale.\n");

        SupplierBean updated = new SupplierBean();
        updated.setSupplierId(current.getSupplierId());

        String name = readString("Nuovo nome [" + text(current.getName()) + "]: ").trim();
        updated.setName(name.isEmpty() ? current.getName() : name);
        updated.setTaxCode(readTaxCodeKeepingCurrent(current.getTaxCode()));
        updated.setAddresses(copyAddresses(current.getAddresses()));
        return updated;
    }

    public List<AddressBean> readUpdatedAddresses(List<AddressBean> currentAddresses) {
        clearScreen();
        ui.showHeader();
        showTitle("MODIFICA INDIRIZZI FORNITORE", ui.theme().warning(), 80);

        showAddresses(currentAddresses);
        if (!readBoolean("Vuoi sostituire tutti gli indirizzi?", false)) return copyAddresses(currentAddresses);

        showMessage("\nInserimento nuovi indirizzi:");
        return readAddresses();
    }

    public int readSupplierId() {
        return readPositiveInt("Inserisci il codice fornitore: ");
    }

    public String readSpeciesCode() {
        while (true) {
            String value = readRequiredString("Inserisci il codice specie: ").trim().toUpperCase();
            PlantSpeciesBean probe = new PlantSpeciesBean();
            try {
                probe.setSpeciesCode(value);
                return probe.getSpeciesCode();
            } catch (IllegalArgumentException e) {
                showError("Codice specie non valido. Formato atteso: SP-F001-IN / SP-N001-ES.");
            }
        }
    }

    public boolean confirmDelete(int supplierId) {
        clearScreen();
        ui.showHeader();
        showTitle("CONFERMA ELIMINAZIONE", ui.theme().error(), 80);
        showWarning("ATTENZIONE: stai per eliminare il fornitore #" + supplierId + ".");
        showWarning("Saranno eliminate anche le associazioni alle specie e gli indirizzi non più utilizzati.");
        showWarning("Questa operazione non può essere annullata.");
        return readBoolean("Sei sicuro di voler procedere?", false);
    }

    public boolean confirmAddSupply(int supplierId, String speciesCode) {
        return readBoolean("Confermi l'associazione della specie " + text(speciesCode) + " al fornitore #" + supplierId + "?", true);
    }

    public boolean confirmRemoveSupply(int supplierId, String speciesCode) {
        return readBoolean("Confermi la rimozione della specie " + text(speciesCode) + " dal fornitore #" + supplierId + "?", false);
    }

    public void showSuppliers(List<SupplierBean> suppliers) {
        clearScreen();
        ui.showHeader();
        showTitle("ELENCO FORNITORI", ui.theme().secondary(), 72);

        if (suppliers == null || suppliers.isEmpty()) {
            showMessage("Nessun fornitore presente nel sistema.");
            return;
        }

        List<String> headers = List.of("CODICE", "NOME", "CODICE FISCALE / P.IVA");
        List<Integer> widths = List.of(10, 32, 24);
        List<List<String>> rows = new ArrayList<>();

        for (SupplierBean supplier : suppliers) {
            rows.add(List.of(
                    text(supplier.getSupplierId()),
                    text(supplier.getName()),
                    text(supplier.getTaxCode())
            ));
        }

        showTable(headers, rows, widths);
        showSuccess("Totale fornitori: " + suppliers.size());
    }

    public void showSupplierDetails(SupplierBean supplier) {
        showSupplierDetails(supplier, true);
    }

    public void showSuppliedSpeciesCodes(List<String> speciesCodes, int supplierId) {
        clearScreen();
        ui.showHeader();
        showTitle("SPECIE FORNITE DAL FORNITORE #" + supplierId, ui.theme().success(), 80);

        if (speciesCodes == null || speciesCodes.isEmpty()) {
            showMessage("Il fornitore non fornisce alcuna specie.");
            return;
        }

        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < speciesCodes.size(); i++) {
            rows.add(List.of(String.valueOf(i + 1), text(speciesCodes.get(i))));
        }
        showTable(List.of("N°", "CODICE SPECIE"), rows, List.of(6, 20));
        showSuccess("Totale specie fornite: " + speciesCodes.size());
    }

    public void showSuppliedSpecies(List<PlantSpeciesBean> species, int supplierId) {
        clearScreen();
        ui.showHeader();
        showTitle("SPECIE FORNITE DAL FORNITORE #" + supplierId, ui.theme().success(), 96);

        if (species == null || species.isEmpty()) {
            showMessage("Il fornitore non fornisce alcuna specie.");
            return;
        }

        List<String> headers = List.of("CODICE", "NOME COMUNE", "NOME LATINO", "TIPOLOGIA", "ESOTICA", "COLORAZIONE");
        List<Integer> widths = List.of(13, 22, 25, 13, 9, 14);
        List<List<String>> rows = new ArrayList<>();

        for (PlantSpeciesBean item : species) {
            rows.add(List.of(
                    text(item.getSpeciesCode()),
                    text(item.getCommonName()),
                    text(item.getLatinName()),
                    plantTypeLabel(item.getPlantType()),
                    Boolean.TRUE.equals(item.getExotic()) ? "Sì" : "No",
                    Boolean.TRUE.equals(item.getFlowering()) ? flowerColorLabel(item.getFlowerColor()) : "-"
            ));
        }

        showTable(headers, rows, widths);
        showSuccess("Totale specie fornite: " + species.size());
    }

    public void showSupplyCheckResult(int supplierId, String speciesCode, boolean supplied) {
        if (supplied) showSuccess("Il fornitore #" + supplierId + " fornisce la specie " + text(speciesCode) + ".");
        else showWarning("Il fornitore #" + supplierId + " non fornisce la specie " + text(speciesCode) + ".");
    }

    public void showCreateSuccess(int supplierId) {
        clearScreen();
        ui.showHeader();
        showSuccess("Fornitore inserito con successo. Codice: " + supplierId);
    }

    public void showUpdateSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Fornitore modificato con successo.");
    }

    public void showDeleteSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Fornitore eliminato con successo.");
    }

    public void showSupplyAddedSuccess() {
        showSuccess("Fornitura della specie associata al fornitore con successo.");
    }

    public void showSupplyRemovedSuccess() {
        showSuccess("Fornitura della specie rimossa dal fornitore con successo.");
    }

    public void showSupplierNotFound(int supplierId) {
        showError("Fornitore non trovato con codice: " + supplierId);
    }

    private List<AddressBean> readAddresses() {
        List<AddressBean> addresses = new ArrayList<>();
        do {
            addresses.add(readAddress());
        } while (readBoolean("Vuoi aggiungere un altro indirizzo?", false));
        return addresses;
    }

    private AddressBean readAddress() {
        showSeparator();
        showMessage("Inserisci i dati dell'indirizzo:");

        int choice = readMenuChoice("Tipo indirizzo", ADDRESS_TYPE_LABELS);
        AddressBean address = new AddressBean();
        address.setType(ADDRESS_TYPE_VALUES.get(choice - 1));
        address.setStreet(readRequiredString("Via e numero civico: ").trim());
        address.setPostalCode(readPostalCode());
        address.setCity(readRequiredString("Città: ").trim());
        return address;
    }

    private String readTaxCode() {
        while (true) {
            String value = readRequiredString("Codice Fiscale / Partita IVA: ").trim().toUpperCase();
            SupplierBean probe = new SupplierBean();
            try {
                probe.setTaxCode(value);
                return probe.getTaxCode();
            } catch (IllegalArgumentException e) {
                showError("Valore non valido. Inserire un codice fiscale di 16 caratteri o una Partita IVA di 11 cifre.");
            }
        }
    }

    private String readTaxCodeKeepingCurrent(String current) {
        while (true) {
            String value = readString("Nuovo Codice Fiscale / Partita IVA [" + text(current) + "]: ").trim().toUpperCase();
            if (value.isEmpty()) return current;
            SupplierBean probe = new SupplierBean();
            try {
                probe.setTaxCode(value);
                return probe.getTaxCode();
            } catch (IllegalArgumentException e) {
                showError("Valore non valido. Inserire un codice fiscale di 16 caratteri o una Partita IVA di 11 cifre.");
            }
        }
    }

    private String readPostalCode() {
        while (true) {
            String value = readRequiredString("CAP: ").trim();
            AddressBean probe = new AddressBean();
            try {
                probe.setPostalCode(value);
                return probe.getPostalCode();
            } catch (IllegalArgumentException e) {
                showError("CAP non valido. Deve essere composto da 5 cifre.");
            }
        }
    }

    private void showSupplierDetails(SupplierBean supplier, boolean clear) {
        if (supplier == null) {
            showWarning("Nessun dato fornitore disponibile.");
            return;
        }

        if (clear) {
            clearScreen();
            ui.showHeader();
        }
        showTitle("DETTAGLIO FORNITORE #" + text(supplier.getSupplierId()), ui.theme().primary(), 80);
        showSeparator();
        showMessage("Nome: " + text(supplier.getName()));
        showMessage("Codice Fiscale / P.IVA: " + text(supplier.getTaxCode()));
        showSeparator();
        showMessage("INDIRIZZI REGISTRATI:");
        showAddresses(supplier.getAddresses());
    }

    private void showAddresses(List<AddressBean> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            showMessage("Nessun indirizzo registrato.");
            return;
        }

        for (int i = 0; i < addresses.size(); i++) {
            AddressBean address = addresses.get(i);
            showMessage((i + 1) + ". " + addressTypeLabel(address.getType()));
            showMessage("   Via: " + text(address.getStreet()));
            showMessage("   Città: " + text(address.getCity()));
            showMessage("   CAP: " + text(address.getPostalCode()));
            if (i < addresses.size() - 1) showMessage("");
        }
    }

    private List<AddressBean> copyAddresses(List<AddressBean> addresses) {
        List<AddressBean> copies = new ArrayList<>();
        if (addresses == null) return copies;
        for (AddressBean address : addresses) {
            if (address == null) continue;
            AddressBean copy = new AddressBean();
            copy.setId(address.getId());
            copy.setType(address.getType());
            copy.setStreet(address.getStreet());
            copy.setPostalCode(address.getPostalCode());
            copy.setCity(address.getCity());
            copies.add(copy);
        }
        return copies;
    }

    private String addressTypeLabel(String type) {
        if (type == null) return "-";
        return switch (type.toUpperCase()) {
            case "LEGAL" -> "LEGALE";
            case "BILLING" -> "FATTURAZIONE";
            default -> type;
        };
    }

    private String plantTypeLabel(String plantType) {
        if (plantType == null) return "-";
        return switch (plantType.toUpperCase()) {
            case "INDOOR" -> "APPARTAMENTO";
            case "GARDEN" -> "GIARDINO";
            default -> plantType;
        };
    }

    private String flowerColorLabel(String color) {
        if (color == null) return "-";
        return switch (color.toUpperCase()) {
            case "RED" -> "ROSSA";
            case "YELLOW" -> "GIALLA";
            case "ORANGE" -> "ARANCIONE";
            case "BLUE" -> "BLU";
            case "PURPLE" -> "VIOLA";
            case "WHITE" -> "BIANCA";
            case "MULTICOLOR" -> "MULTICOLORE";
            default -> color;
        };
    }

    private String text(Object value) {
        return value == null ? "-" : String.valueOf(value);
    }
}
