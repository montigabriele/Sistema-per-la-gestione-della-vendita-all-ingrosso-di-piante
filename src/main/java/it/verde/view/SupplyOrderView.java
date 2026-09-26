package it.verde.view;

import it.verde.view.bean.PlantSpeciesBean;
import it.verde.view.bean.SupplierBean;
import it.verde.view.bean.SupplyOrderBean;
import it.verde.view.bean.SupplyOrderItemBean;
import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.util.ArrayList;
import java.util.List;

public final class SupplyOrderView extends AbstractOrderView {
    private static final String SUPPLIER_PREFIX = "Fornitore: ";

    private static final List<String> MAIN_OPTIONS = List.of(
            "Crea ordine di rifornimento",
            "Visualizza ordini di rifornimento",
            "Modifica dati ordine di rifornimento",
            "Elimina ordine di rifornimento",
            "Cambia stato ordine",
            "Ricerca avanzata",
            "Torna al Menù Principale"
    );

    private static final List<String> SEARCH_OPTIONS = List.of(
            "Ricerca ordini per stato",
            "Ricerca ordini per fornitore",
            "Torna al menù ordini di rifornimento"
    );

    private static final String DEFAULT_CONTACT_EMAIL = "magazzino@verdesrl.it";
    private static final String DEFAULT_DELIVERY_STREET = "Via delle Piante 10";
    private static final String DEFAULT_DELIVERY_CITY = "Roma";
    private static final String DEFAULT_DELIVERY_POSTAL_CODE = "00100";

    public SupplyOrderView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    @Override
    protected boolean colorLocalizedStatuses() {
        return true;
    }

    public int showMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("GESTIONE ORDINI DI RIFORNIMENTO", ui.theme().primary(), 80);
        return readMenuChoice("Gestione ordini di rifornimento", MAIN_OPTIONS);
    }

    public int showSearchMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("RICERCA ORDINI DI RIFORNIMENTO", ui.theme().secondary(), 80);
        return readMenuChoice("Seleziona un tipo di ricerca", SEARCH_OPTIONS);
    }

    public SupplyOrderBean readNewOrder(int supplierId) {
        clearScreen();
        ui.showHeader();
        showTitle("INSERIMENTO ORDINE DI RIFORNIMENTO", ui.theme().primary(), 80);

        SupplyOrderBean bean = new SupplyOrderBean();
        bean.setSupplierId(supplierId);
        bean.setOrderDate(currentDate());
        bean.setStatus(STATUS_OPEN);

        showSeparator();
        showMessage("Email predefinita: " + DEFAULT_CONTACT_EMAIL);
        if (readBoolean("Vuoi modificare la mail?", false)) bean.setContactEmail(readEmail());
        else bean.setContactEmail(DEFAULT_CONTACT_EMAIL);

        showSeparator();
        showMessage("INDIRIZZO DI CONSEGNA MAGAZZINO VERDE SRL");
        showMessage("Via: " + DEFAULT_DELIVERY_STREET);
        showMessage("Città: " + DEFAULT_DELIVERY_CITY);
        showMessage("CAP: " + DEFAULT_DELIVERY_POSTAL_CODE);

        if (readBoolean("Vuoi modificare l'indirizzo di consegna?", false)) {
            bean.setDeliveryStreet(readRequiredString("Nuova Via: ").trim());
            bean.setDeliveryCity(readRequiredString("Nuova Città: ").trim());
            bean.setDeliveryPostalCode(readPostalCode("Nuovo CAP (5 cifre): "));
        } else {
            bean.setDeliveryStreet(DEFAULT_DELIVERY_STREET);
            bean.setDeliveryCity(DEFAULT_DELIVERY_CITY);
            bean.setDeliveryPostalCode(DEFAULT_DELIVERY_POSTAL_CODE);
        }

        showSeparator();
        showMessage(COURIER_CONTACT_TITLE);
        showSeparator();
        bean.setContactPerson(readRequiredString("Nome e cognome referente: ").trim());
        bean.setCourierContact(readPhone("Recapito telefonico: "));

        showNewOrderItemsIntro();
        return bean;
    }

    public SupplyOrderItemBean readOrderItem() {
        return readOrderItem(
                SupplyOrderItemBean::new,
                String::trim,
                SupplyOrderItemBean::setSpeciesCode,
                SupplyOrderItemBean::setQuantity
        );
    }

    public SupplyOrderBean readUpdatedOrder(SupplyOrderBean current) {
        if (current == null) throw new IllegalArgumentException("Current supply order cannot be null");

        clearScreen();
        ui.showHeader();
        showTitle("MODIFICA ORDINE DI RIFORNIMENTO #" + text(current.getOrderId()), ui.theme().primary(), 80);
        showMessage("Lasciare vuoto per mantenere il valore attuale.");
        showSeparator();

        SupplyOrderBean updated = new SupplyOrderBean();
        updated.setOrderId(current.getOrderId());
        updated.setOrderDate(current.getOrderDate());
        updated.setStatus(current.getStatus());
        updated.setSupplierId(current.getSupplierId());
        updated.setItems(current.getItems());

        updated.setContactEmail(readEmailKeepingCurrent(current.getContactEmail()));

        showSeparator();
        showMessage(DELIVERY_ADDRESS_TITLE);
        showSeparator();
        updated.setDeliveryStreet(readKeepingCurrent("Via", current.getDeliveryStreet()));
        updated.setDeliveryCity(readKeepingCurrent("Città", current.getDeliveryCity()));
        updated.setDeliveryPostalCode(readPostalCodeKeepingCurrent(current.getDeliveryPostalCode()));

        showSeparator();
        showMessage(COURIER_CONTACT_TITLE);
        showSeparator();
        updated.setContactPerson(readKeepingCurrent("Nome e cognome referente", current.getContactPerson()));
        updated.setCourierContact(readPhoneKeepingCurrent(current.getCourierContact()));
        return updated;
    }

    public int readSupplierId() {
        return readPositiveInt("Inserisci il codice del fornitore: ");
    }

    public String readStatus() {
        return readStatusChoice("Seleziona lo stato degli ordini da visualizzare");
    }

    public String readNewStatus() {
        List<String> options = List.of(
                "APERTO - Ordine aperto",
                "CONFERMATO - Ordine confermato dal fornitore",
                "SPEDITO - Ordine spedito dal fornitore",
                "CONSEGNATO - Ordine consegnato",
                "ANNULLATO - Ordine cancellato"
        );
        return chooseNewStatus(options);
    }

    public boolean confirmCreation(SupplyOrderBean order) {
        if (order == null) throw new IllegalArgumentException("Supply order cannot be null");
        showSeparator();
        showMessage("RIEPILOGO ORDINE:");
        showMessage(SUPPLIER_PREFIX + text(order.getSupplierId()));
        showMessage("Email: " + text(order.getContactEmail()));
        showMessage("Consegna: " + text(order.getDeliveryStreet()) + ", " + text(order.getDeliveryCity()) + " " + text(order.getDeliveryPostalCode()));
        showMessage("Referente: " + text(order.getContactPerson()));
        showMessage("Recapito: " + text(order.getCourierContact()));
        showMessage("Articoli: " + (order.getItems() == null ? 0 : order.getItems().size()));
        return readBoolean("Confermi la creazione dell'ordine?", true);
    }

    public boolean confirmStatusChange(SupplyOrderBean order, String newStatus) {
        if (order == null) throw new IllegalArgumentException("Supply order cannot be null");
        return readBoolean("Confermi il cambio di stato dell'ordine #" + order.getOrderId() + " da \"" + statusLabel(order.getStatus()) + "\" a \"" + statusLabel(newStatus) + "\"?", true);
    }

    public boolean confirmItem(SupplyOrderItemBean item) {
        if (item == null) throw new IllegalArgumentException("Supply order item cannot be null");
        showMessage("Riepilogo articolo:");
        showMessage("Specie: " + text(item.getSpeciesCode()));
        showMessage("Quantità: " + text(item.getQuantity()));
        return readBoolean("Confermi l'aggiunta di questo articolo?", true);
    }

    public void showOrderDetails(SupplyOrderBean order, SupplierBean supplier) {
        if (order == null) throw new IllegalArgumentException("Supply order cannot be null");

        clearScreen();
        ui.showHeader();
        showTitle("DETTAGLIO ORDINE RIFORNIMENTO #" + text(order.getOrderId()), ui.theme().primary(), 80);
        showSeparator();
        showMessage("Data: " + (order.getOrderDate() == null ? "-" : order.getOrderDate().format(DATE_FORMATTER)));
        if (supplier != null) showMessage(SUPPLIER_PREFIX + text(supplier.getName()) + " (" + text(supplier.getSupplierId()) + ")");
        else showMessage(SUPPLIER_PREFIX + text(order.getSupplierId()));
        showMessage("Email: " + text(order.getContactEmail()));
        showMessage("Stato: " + coloredStatus(order.getStatus()));

        showDeliveryAddress(
                text(order.getDeliveryStreet()),
                text(order.getDeliveryCity()),
                text(order.getDeliveryPostalCode())
        );
        showCourierContact(text(order.getContactPerson()), text(order.getCourierContact()));

        showSeparator();
        showMessage("ARTICOLI ORDINATI");
        showSeparator();
        showOrderItems(order.getItems());
        showSeparator();
        waitForEnter();
    }

    public void showOrders(List<SupplyOrderBean> orders) {
        clearScreen();
        ui.showHeader();
        showTitle("ELENCO ORDINI DI RIFORNIMENTO", ui.theme().primary(), 88);
        showOrdersTable(orders);
    }

    public void showOrdersBySupplier(List<SupplyOrderBean> orders, SupplierBean supplier) {
        clearScreen();
        ui.showHeader();
        String title = supplier == null ? "ORDINI PER FORNITORE" : "ORDINI PER FORNITORE: " + text(supplier.getName());
        showTitle(title, ui.theme().primary(), 88);
        showOrdersTable(orders);
        waitForEnter();
    }

    public void showOrdersByStatus(List<SupplyOrderBean> orders, String status) {
        clearScreen();
        ui.showHeader();
        showTitle("ORDINI IN STATO: " + statusLabel(status), ui.theme().primary(), 88);
        showOrdersTable(orders);
        waitForEnter();
    }

    public void showSuppliers(List<SupplierBean> suppliers) {
        clearScreen();
        ui.showHeader();
        showTitle("ELENCO FORNITORI", ui.theme().secondary(), 72);

        if (suppliers == null || suppliers.isEmpty()) {
            showWarning("Nessun fornitore presente nel sistema.");
            return;
        }

        List<String> headers = List.of("CODICE", "NOME", "CODICE FISCALE/P.IVA");
        List<Integer> widths = List.of(10, 30, 24);
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

    public void showSuppliedSpecies(List<PlantSpeciesBean> species, int supplierId) {
        clearScreen();
        ui.showHeader();
        showTitle("SPECIE FORNITE DAL FORNITORE #" + supplierId, ui.theme().secondary(), 96);

        if (species == null || species.isEmpty()) {
            showMessage("Nessuna specie fornita da questo fornitore.");
            return;
        }

        List<String> headers = List.of("CODICE", "NOME LATINO", "NOME COMUNE", "TIPOLOGIA", "ESOTICA", "COLORAZIONE");
        List<Integer> widths = List.of(13, 25, 22, 13, 9, 14);
        List<List<String>> rows = new ArrayList<>();

        for (PlantSpeciesBean item : species) {
            rows.add(List.of(
                    text(item.getSpeciesCode()),
                    text(item.getLatinName()),
                    text(item.getCommonName()),
                    plantTypeLabel(item.getPlantType()),
                    Boolean.TRUE.equals(item.getExotic()) ? "Sì" : "No",
                    Boolean.TRUE.equals(item.getFlowering()) ? flowerColorLabel(item.getFlowerColor()) : "-"
            ));
        }

        showTable(headers, rows, widths);
        showSuccess("Totale specie disponibili: " + species.size());
    }

    public void showStockInfo(String speciesCode, int currentQuantity) {
        showMessage("Informazioni giacenza per specie " + text(speciesCode) + ":");
        showMessage("Giacenza attuale: " + currentQuantity + " unità");
        if (currentQuantity <= 10) showWarning("ATTENZIONE: Giacenza bassa!");
    }

    public void showItemAdded(SupplyOrderItemBean item) {
        if (item == null) return;
        showSuccess("Dettaglio aggiunto: " + text(item.getSpeciesCode()) + " x" + text(item.getQuantity()));
    }

    public void showCreateSuccess(int orderId) {
        clearScreen();
        ui.showHeader();
        showSuccess("Ordine di rifornimento creato con successo. ID: " + orderId);
    }

    public void showUpdateSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Ordine di rifornimento modificato con successo.");
    }

    public void showDeleteSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Ordine di rifornimento eliminato con successo.");
    }

    public void showStatusChangeSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Stato dell'ordine di rifornimento modificato con successo.");
    }

    public void showOrderNotFound(int orderId) {
        showError("Ordine di rifornimento non trovato con ID: " + orderId);
    }

    public void showSupplierNotFound(int supplierId) {
        showError("Fornitore non trovato con codice: " + supplierId);
    }

    private void showOrdersTable(List<SupplyOrderBean> orders) {
        if (orders == null || orders.isEmpty()) {
            showMessage("Nessun ordine di rifornimento presente.");
            return;
        }

        List<String> headers = List.of("ID", "COD. FORNITORE", "DATA ORDINE", "STATO", "CITTÀ");
        List<Integer> widths = List.of(7, 16, 14, 14, 22);
        List<List<String>> rows = new ArrayList<>();

        for (SupplyOrderBean order : orders) {
            rows.add(List.of(
                    text(order.getOrderId()),
                    text(order.getSupplierId()),
                    order.getOrderDate() == null ? "-" : order.getOrderDate().format(DATE_FORMATTER),
                    statusLabel(order.getStatus()),
                    text(order.getDeliveryCity())
            ));
        }

        showTable(headers, rows, widths);
        showMessage("Totale ordini: " + orders.size());
    }

    private void showOrderItems(List<SupplyOrderItemBean> items) {
        if (items == null || items.isEmpty()) {
            showMessage("Nessun articolo presente nell'ordine.");
            return;
        }

        List<String> headers = List.of("CODICE SPECIE", "QUANTITÀ");
        List<Integer> widths = List.of(18, 12);
        List<List<String>> rows = new ArrayList<>();

        for (SupplyOrderItemBean item : items) {
            rows.add(List.of(text(item.getSpeciesCode()), text(item.getQuantity())));
        }
        showTable(headers, rows, widths);
    }

    private String readEmail() {
        while (true) {
            String value = readRequiredString("Nuova Email: ").trim();
            SupplyOrderBean probe = new SupplyOrderBean();
            try {
                probe.setContactEmail(value);
                return probe.getContactEmail();
            } catch (IllegalArgumentException e) {
                showError("Email inserita non valida, riprova.");
            }
        }
    }

    private String readPostalCode(String prompt) {
        while (true) {
            String value = readRequiredString(prompt).trim();
            SupplyOrderBean probe = new SupplyOrderBean();
            try {
                probe.setDeliveryPostalCode(value);
                return probe.getDeliveryPostalCode();
            } catch (IllegalArgumentException e) {
                showError("CAP inserito non valido. Deve essere composto da 5 cifre.");
            }
        }
    }

    private String readPhone(String prompt) {
        while (true) {
            String value = readRequiredString(prompt).trim();
            SupplyOrderBean probe = new SupplyOrderBean();
            try {
                probe.setCourierContact(value);
                return probe.getCourierContact();
            } catch (IllegalArgumentException e) {
                showError("Numero di telefono inserito non valido.");
            }
        }
    }

    private String readKeepingCurrent(String label, String currentValue) {
        String value = readString(label + " [" + text(currentValue) + "]: ").trim();
        return value.isEmpty() ? currentValue : value;
    }

    private String readEmailKeepingCurrent(String currentValue) {
        while (true) {
            String value = readString("Email contatto [" + text(currentValue) + "]: ").trim();
            if (value.isEmpty()) return currentValue;
            SupplyOrderBean probe = new SupplyOrderBean();
            try {
                probe.setContactEmail(value);
                return probe.getContactEmail();
            } catch (IllegalArgumentException e) {
                showError("Email inserita non valida, riprova.");
            }
        }
    }

    private String readPostalCodeKeepingCurrent(String currentValue) {
        while (true) {
            String value = readString("CAP [" + text(currentValue) + "]: ").trim();
            if (value.isEmpty()) return currentValue;
            SupplyOrderBean probe = new SupplyOrderBean();
            try {
                probe.setDeliveryPostalCode(value);
                return probe.getDeliveryPostalCode();
            } catch (IllegalArgumentException e) {
                showError("CAP inserito non valido. Deve essere composto da 5 cifre.");
            }
        }
    }

    private String readPhoneKeepingCurrent(String currentValue) {
        while (true) {
            String value = readString("Recapito corriere [" + text(currentValue) + "]: ").trim();
            if (value.isEmpty()) return currentValue;
            SupplyOrderBean probe = new SupplyOrderBean();
            try {
                probe.setCourierContact(value);
                return probe.getCourierContact();
            } catch (IllegalArgumentException e) {
                showError("Numero di telefono inserito non valido.");
            }
        }
    }

    private String plantTypeLabel(String plantType) {
        if (plantType == null || plantType.isBlank()) return "-";
        return switch (plantType.toUpperCase()) {
            case "INDOOR" -> "APPARTAMENTO";
            case "GARDEN" -> "GIARDINO";
            default -> plantType;
        };
    }

    private String flowerColorLabel(String color) {
        if (color == null || color.isBlank()) return "-";
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
