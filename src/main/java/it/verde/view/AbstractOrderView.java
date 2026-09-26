package it.verde.view;

import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

abstract class AbstractOrderView extends BaseView {
    protected static final String STATUS_OPEN = "OPEN";
    protected static final String STATUS_CONFIRMED = "CONFIRMED";
    protected static final String STATUS_SHIPPED = "SHIPPED";
    protected static final String STATUS_DELIVERED = "DELIVERED";
    protected static final String STATUS_CANCELLED = "CANCELLED";

    private static final String STATUS_LABEL_OPEN = "APERTO";
    private static final String STATUS_LABEL_CONFIRMED = "CONFERMATO";
    private static final String STATUS_LABEL_SHIPPED = "SPEDITO";
    private static final String STATUS_LABEL_DELIVERED = "CONSEGNATO";
    private static final String STATUS_LABEL_CANCELLED = "ANNULLATO";

    protected static final String DELIVERY_ADDRESS_TITLE = "INDIRIZZO DI CONSEGNA";
    protected static final String COURIER_CONTACT_TITLE = "CONTATTO PER IL CORRIERE";
    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final List<String> STATUS_LABELS = List.of(
            STATUS_LABEL_OPEN,
            STATUS_LABEL_CONFIRMED,
            STATUS_LABEL_SHIPPED,
            STATUS_LABEL_DELIVERED,
            STATUS_LABEL_CANCELLED
    );

    private static final List<String> STATUS_VALUES = List.of(
            STATUS_OPEN,
            STATUS_CONFIRMED,
            STATUS_SHIPPED,
            STATUS_DELIVERED,
            STATUS_CANCELLED
    );

    protected AbstractOrderView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    protected abstract boolean colorLocalizedStatuses();

    protected final LocalDate currentDate() {
        return LocalDate.now(ZoneId.systemDefault());
    }

    public final int readOrderId() {
        return readPositiveInt("Inserisci l'ID dell'ordine: ");
    }

    public final boolean askShowOrderDetails() {
        return readBoolean("Vuoi visualizzare i dettagli di un ordine?", false);
    }

    public final boolean askAddAnotherItem() {
        return readBoolean("Vuoi aggiungere un altro articolo all'ordine?", false);
    }

    public final boolean confirmDelete(int orderId) {
        return readBoolean("Sei sicuro di voler eliminare l'ordine #" + orderId + "? Questa operazione non può essere annullata.", false);
    }

    protected final String readStatusChoice(String prompt) {
        int choice = readMenuChoice(prompt, STATUS_LABELS);
        return statusValueForChoice(choice);
    }

    protected final String statusValueForChoice(int choice) {
        return STATUS_VALUES.get(choice - 1);
    }


    protected final String chooseNewStatus(List<String> options) {
        showTitle("STATI DISPONIBILI", ui.theme().primary(), 64);
        int choice = readMenuChoice("Seleziona il nuovo stato dell'ordine", options);
        return statusValueForChoice(choice);
    }

    protected final <T> T readOrderItem(
            Supplier<T> itemFactory,
            UnaryOperator<String> normalizeSpeciesCode,
            BiConsumer<T, String> speciesCodeSetter,
            ObjIntConsumer<T> quantitySetter
    ) {
        T item = itemFactory.get();
        while (true) {
            try {
                String code = normalizeSpeciesCode.apply(readRequiredString("Codice specie: "));
                speciesCodeSetter.accept(item, code);
                break;
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        }
        quantitySetter.accept(item, readPositiveInt("Quantità: "));
        return item;
    }

    protected final void showNewOrderItemsIntro() {
        showSeparator();
        showMessage("ARTICOLI DELL'ORDINE");
        showSeparator();
        showMessage("Procedi con l'inserimento degli articoli.");
    }

    protected final void showDeliveryAddress(String street, String city, String postalCode) {
        showSeparator();
        showMessage(DELIVERY_ADDRESS_TITLE);
        showSeparator();
        showMessage("Via: " + street);
        showMessage("Città: " + city);
        showMessage("CAP: " + postalCode);
    }

    protected final void showCourierContact(String contactPerson, String courierContact) {
        showSeparator();
        showMessage(COURIER_CONTACT_TITLE);
        showSeparator();
        showMessage("Referente: " + contactPerson);
        showMessage("Recapito: " + courierContact);
    }

    protected final String coloredStatus(String status) {
        String label = statusLabel(status);
        if (status == null) return label;

        String normalizedStatus = status.toUpperCase();
        String color = switch (normalizedStatus) {
            case STATUS_OPEN -> ui.theme().info();
            case STATUS_CONFIRMED -> ui.theme().accent();
            case STATUS_SHIPPED -> ui.theme().primary();
            case STATUS_DELIVERED -> ui.theme().success();
            case STATUS_CANCELLED -> ui.theme().error();
            case STATUS_LABEL_OPEN -> colorLocalizedStatuses() ? ui.theme().info() : null;
            case STATUS_LABEL_CONFIRMED -> colorLocalizedStatuses() ? ui.theme().accent() : null;
            case STATUS_LABEL_SHIPPED -> colorLocalizedStatuses() ? ui.theme().primary() : null;
            case STATUS_LABEL_DELIVERED -> colorLocalizedStatuses() ? ui.theme().success() : null;
            case STATUS_LABEL_CANCELLED -> colorLocalizedStatuses() ? ui.theme().error() : null;
            default -> null;
        };
        return color == null ? label : ui.theme().colorize(label, color);
    }

    protected final String statusLabel(String status) {
        if (status == null || status.isBlank()) return "-";
        return switch (status.toUpperCase()) {
            case STATUS_OPEN, STATUS_LABEL_OPEN -> STATUS_LABEL_OPEN;
            case STATUS_CONFIRMED, STATUS_LABEL_CONFIRMED -> STATUS_LABEL_CONFIRMED;
            case STATUS_SHIPPED, STATUS_LABEL_SHIPPED -> STATUS_LABEL_SHIPPED;
            case STATUS_DELIVERED, STATUS_LABEL_DELIVERED -> STATUS_LABEL_DELIVERED;
            case STATUS_CANCELLED, STATUS_LABEL_CANCELLED -> STATUS_LABEL_CANCELLED;
            default -> status;
        };
    }
}
