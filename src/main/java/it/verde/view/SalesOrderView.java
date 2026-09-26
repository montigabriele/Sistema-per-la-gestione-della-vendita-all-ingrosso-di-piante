package it.verde.view;

import it.verde.view.bean.SalesOrderBean;
import it.verde.view.bean.SalesOrderItemBean;
import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public final class SalesOrderView extends AbstractOrderView {
    private static final String UNIT_SUFFIX = " unità.";

    private static final List<String> MAIN_OPTIONS = List.of(
            "Inserisci ordine di vendita",
            "Visualizza ordini di vendita",
            "Modifica dati ordine di vendita",
            "Elimina ordine di vendita",
            "Cambia stato ordine",
            "Ricerca avanzata",
            "Torna al Menù Principale"
    );

    private static final List<String> SEARCH_OPTIONS = List.of(
            "Visualizza dettagli ordine",
            "Cerca ordine per stato",
            "Cerca ordine per azienda",
            "Torna al Menù Ordini"
    );

    private static final String NO_CONTACT_PERSON = "Nessun referente associato all'ordine";

    public SalesOrderView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    @Override
    protected boolean colorLocalizedStatuses() {
        return false;
    }

    public int showMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("GESTIONE ORDINI DI VENDITA", ui.theme().info(), 80);
        return readMenuChoice("Gestione ordini di vendita", MAIN_OPTIONS);
    }

    public int showSearchMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("Ricerca Ordine di Vendita", ui.theme().secondary(), 80);
        return readMenuChoice("Seleziona un tipo di ricerca", SEARCH_OPTIONS);
    }

    public SalesOrderBean readNewOrder() {
        clearScreen();
        ui.showHeader();
        showTitle("INSERIMENTO NUOVO ORDINE", ui.theme().primary(), 80);

        SalesOrderBean bean = new SalesOrderBean();
        bean.setOrderDate(currentDate());
        bean.setStatus(STATUS_OPEN);
        bean.setCustomerVatNumber(readVatNumber());

        showSeparator();
        showMessage(DELIVERY_ADDRESS_TITLE);
        showSeparator();
        bean.setDeliveryStreet(readRequiredString("Via: ").toUpperCase());
        bean.setDeliveryCity(readRequiredString("Città: ").toUpperCase());
        bean.setDeliveryPostalCode(readPostalCode());

        showSeparator();
        showMessage(COURIER_CONTACT_TITLE);
        showSeparator();
        String contactPerson = readString("Nome e cognome referente (facoltativo): ").trim();
        bean.setContactPerson(contactPerson.isEmpty() ? NO_CONTACT_PERSON : contactPerson.toUpperCase());
        bean.setCourierContact(readPhone());

        showNewOrderItemsIntro();
        return bean;
    }

    public SalesOrderItemBean readOrderItem() {
        return readOrderItem(
                SalesOrderItemBean::new,
                String::toUpperCase,
                SalesOrderItemBean::setSpeciesCode,
                SalesOrderItemBean::setQuantity
        );
    }

    public SalesOrderBean readUpdatedOrder(SalesOrderBean current) {
        if (current == null) throw new IllegalArgumentException("Current sales order cannot be null");

        clearScreen();
        ui.showHeader();
        showTitle("MODIFICA ORDINE #" + text(current.getOrderId()), ui.theme().primary(), 80);

        SalesOrderBean updated = new SalesOrderBean();
        updated.setOrderId(current.getOrderId());
        updated.setCustomerVatNumber(current.getCustomerVatNumber());
        updated.setOrderDate(current.getOrderDate());
        updated.setStatus(current.getStatus());
        updated.setItems(current.getItems());
        updated.setTotal(current.getTotal());

        showMessage(DELIVERY_ADDRESS_TITLE);
        showSeparator();
        updated.setDeliveryStreet(readKeepingCurrent("Via", current.getDeliveryStreet()));
        updated.setDeliveryCity(readKeepingCurrent("Città", current.getDeliveryCity()));
        updated.setDeliveryPostalCode(readPostalCodeKeepingCurrent(current.getDeliveryPostalCode()));

        showSeparator();
        showMessage(COURIER_CONTACT_TITLE);
        showSeparator();
        updated.setContactPerson(readKeepingCurrent("Referente", current.getContactPerson()));
        updated.setCourierContact(readPhoneKeepingCurrent(current.getCourierContact()));
        return updated;
    }

    public String readCustomerVatNumber() {
        return readVatNumber();
    }

    public String readStatus() {
        return readStatusChoice("Seleziona lo stato dell'ordine di vendita");
    }

    public String readNewStatus() {
        List<String> options = List.of(
                "APERTO - Ordine inserito ma non ancora processato",
                "CONFERMATO - Ordine pronto per la spedizione",
                "SPEDITO - Ordine affidato al corriere",
                "CONSEGNATO - Ordine consegnato al cliente",
                "ANNULLATO - Ordine cancellato"
        );
        return chooseNewStatus(options);
    }

    public boolean confirmStatusChange(SalesOrderBean order, String newStatus) {
        if (order == null) throw new IllegalArgumentException("Sales order cannot be null");
        return readBoolean("Confermi il cambio di stato dell'ordine con ID " + order.getOrderId() + " da \"" + statusLabel(order.getStatus()) + "\" a \"" + statusLabel(newStatus) + "\"?", true);
    }

    public void showAvailability(String speciesCode, int availableQuantity) {
        showMessage("Specie: " + text(speciesCode));
        showMessage("Disponibilità: " + availableQuantity + UNIT_SUFFIX);
    }

    public void showAvailabilityRequest(String speciesCode, int availableQuantity, int requestedQuantity) {
        showMessage("Specie: " + text(speciesCode));
        showMessage("Disponibilità: " + availableQuantity + UNIT_SUFFIX);
        showMessage("Quantità richiesta: " + requestedQuantity + UNIT_SUFFIX);

        if (availableQuantity >= requestedQuantity) showSuccess("Disponibilità sufficiente.");
        else if (availableQuantity > 0) showWarning("Attenzione: disponibilità parziale! Disponibili solo " + availableQuantity + UNIT_SUFFIX);
        else showError("Attenzione: articolo non disponibile!");
    }

    public void showOrders(List<SalesOrderBean> orders) {
        clearScreen();
        ui.showHeader();
        showTitle("ELENCO ORDINI", ui.theme().primary(), 96);
        showOrdersTable(orders);
    }

    public void showOrdersByStatus(List<SalesOrderBean> orders, String status) {
        clearScreen();
        ui.showHeader();
        showTitle("ORDINI IN STATO: " + statusLabel(status), ui.theme().primary(), 96);
        showOrdersTable(orders);
        waitForEnter();
    }

    public void showOrdersByCustomer(List<SalesOrderBean> orders, String vatNumber) {
        clearScreen();
        ui.showHeader();
        showTitle("ORDINI PER AZIENDA: " + text(vatNumber), ui.theme().primary(), 96);
        showOrdersTable(orders);
        waitForEnter();
    }

    public void showOrderDetails(SalesOrderBean order) {
        if (order == null) throw new IllegalArgumentException("Sales order cannot be null");

        clearScreen();
        ui.showHeader();
        showTitle("DETTAGLIO ORDINE #" + text(order.getOrderId()), ui.theme().primary(), 72);
        showSeparator();
        showMessage("Data: " + (order.getOrderDate() == null ? "-" : order.getOrderDate().format(DATE_FORMATTER)));
        showMessage("Partita IVA: " + text(order.getCustomerVatNumber()));
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
        showSuccess("TOTALE ORDINE: €" + formatMoney(order.getTotal()));
        showSeparator();
        waitForEnter();
    }

    public void showCreateSuccess(int orderId) {
        clearScreen();
        ui.showHeader();
        showSuccess("Ordine inserito con successo. ID: " + orderId);
    }

    public void showUpdateSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Ordine modificato con successo.");
    }

    public void showDeleteSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Ordine eliminato con successo.");
    }

    public void showStatusChangeSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Stato modificato con successo.");
    }

    public void showOrderNotFound(int orderId) {
        showError("Ordine non trovato con ID: " + orderId);
    }

    private void showOrdersTable(List<SalesOrderBean> orders) {
        if (orders == null || orders.isEmpty()) {
            showMessage("Nessun ordine presente.");
            return;
        }

        List<String> headers = List.of("ID", "P.IVA CLIENTE", "DATA ORDINE", "TOTALE (€)", "STATO", "CITTÀ");
        List<Integer> widths = List.of(7, 15, 13, 13, 14, 22);
        List<List<String>> rows = new ArrayList<>();

        for (SalesOrderBean order : orders) {
            rows.add(List.of(
                    text(order.getOrderId()),
                    text(order.getCustomerVatNumber()),
                    order.getOrderDate() == null ? "-" : order.getOrderDate().format(DATE_FORMATTER),
                    formatMoney(order.getTotal()),
                    statusLabel(order.getStatus()),
                    text(order.getDeliveryCity())
            ));
        }

        showTable(headers, rows, widths);
        showMessage("Totale ordini: " + orders.size());
    }

    private void showOrderItems(List<SalesOrderItemBean> items) {
        if (items == null || items.isEmpty()) {
            showMessage("Nessun articolo associato all'ordine.");
            return;
        }

        List<String> headers = List.of("CODICE", "QTÀ", "PREZZO (€)", "TOTALE (€)");
        List<Integer> widths = List.of(15, 8, 14, 14);
        List<List<String>> rows = new ArrayList<>();

        for (SalesOrderItemBean item : items) {
            rows.add(List.of(
                    text(item.getSpeciesCode()),
                    text(item.getQuantity()),
                    formatMoney(item.getUnitPrice()),
                    formatMoney(item.getLineTotal())
            ));
        }
        showTable(headers, rows, widths);
    }

    private String readVatNumber() {
        while (true) {
            String value = readRequiredString("Partita IVA cliente (11 cifre numeriche): ").trim();
            SalesOrderBean probe = new SalesOrderBean();
            try {
                probe.setCustomerVatNumber(value);
                return probe.getCustomerVatNumber();
            } catch (IllegalArgumentException e) {
                showError("Partita IVA non valida. Inserire esattamente 11 cifre numeriche.");
            }
        }
    }

    private String readPostalCode() {
        while (true) {
            String value = readRequiredString("CAP (5 cifre): ").trim();
            SalesOrderBean probe = new SalesOrderBean();
            try {
                probe.setDeliveryPostalCode(value);
                return probe.getDeliveryPostalCode();
            } catch (IllegalArgumentException e) {
                showError("CAP non valido. Inserire esattamente 5 cifre numeriche.");
            }
        }
    }

    private String readPhone() {
        while (true) {
            String value = readRequiredString("Recapito telefonico: ").trim();
            SalesOrderBean probe = new SalesOrderBean();
            try {
                probe.setCourierContact(value);
                return probe.getCourierContact();
            } catch (IllegalArgumentException e) {
                showError("Formato telefono non valido. Inserire un numero di 8-15 cifre, opzionalmente con + iniziale.");
            }
        }
    }

    private String readKeepingCurrent(String label, String currentValue) {
        String value = readString(label + " attuale: " + text(currentValue) + "\nNuovo valore [INVIO per mantenere]: ").trim();
        return value.isEmpty() ? currentValue : value;
    }

    private String readPostalCodeKeepingCurrent(String currentValue) {
        while (true) {
            String value = readString("CAP attuale: " + text(currentValue) + "\nNuovo CAP [INVIO per mantenere]: ").trim();
            if (value.isEmpty()) return currentValue;
            SalesOrderBean probe = new SalesOrderBean();
            try {
                probe.setDeliveryPostalCode(value);
                return probe.getDeliveryPostalCode();
            } catch (IllegalArgumentException e) {
                showError("CAP non valido. Inserire esattamente 5 cifre numeriche.");
            }
        }
    }

    private String readPhoneKeepingCurrent(String currentValue) {
        while (true) {
            String value = readString("Recapito attuale: " + text(currentValue) + "\nNuovo recapito [INVIO per mantenere]: ").trim();
            if (value.isEmpty()) return currentValue;
            SalesOrderBean probe = new SalesOrderBean();
            try {
                probe.setCourierContact(value);
                return probe.getCourierContact();
            } catch (IllegalArgumentException e) {
                showError("Formato telefono non valido. Inserire un numero di 8-15 cifre, opzionalmente con + iniziale.");
            }
        }
    }

    private String formatMoney(BigDecimal value) {
        return value == null ? "0.00" : value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String text(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
