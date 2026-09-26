package it.verde.view;

import it.verde.view.bean.AddressBean;
import it.verde.view.bean.ContactBean;
import it.verde.view.bean.RetailCompanyBean;
import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class RetailCompanyView extends BaseView {
    private static final String CONTACT_LABEL_PHONE = "TELEFONO";
    private static final String CONTACT_LABEL_MOBILE = "CELLULARE";
    private static final String CONTACT_TYPE_PHONE = "PHONE";
    private static final String CONTACT_TYPE_MOBILE = "MOBILE";
    private static final String CONTACT_TYPE_EMAIL = "EMAIL";
    private static final String ADDRESS_TYPE_BILLING = "BILLING";

    private static final List<String> MAIN_OPTIONS = List.of(
            "Inserisci nuova azienda",
            "Visualizza elenco aziende",
            "Modifica azienda",
            "Elimina azienda",
            "Cerca azienda per Partita IVA",
            "Torna al Menù Principale"
    );

    private static final List<String> CONTACT_TYPE_LABELS = List.of(CONTACT_LABEL_PHONE, CONTACT_LABEL_MOBILE, CONTACT_TYPE_EMAIL);
    private static final List<String> CONTACT_TYPE_VALUES = List.of(CONTACT_TYPE_PHONE, CONTACT_TYPE_MOBILE, CONTACT_TYPE_EMAIL);

    public RetailCompanyView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    public int showMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("Gestione Aziende Rivenditrici", ui.theme().info(), 80);
        return readMenuChoice("Menu Aziende Rivenditrici", MAIN_OPTIONS);
    }

    public RetailCompanyBean readNewCompany() {
        clearScreen();
        ui.showHeader();
        showTitle("Inserimento Nuova Azienda", ui.theme().success(), 80);

        RetailCompanyBean bean = new RetailCompanyBean();
        bean.setVatNumber(readVatNumber());
        bean.setCompanyName(readRequiredUppercase("Nome Azienda: "));
        bean.setContactFirstName(readRequiredUppercase("Nome Referente: "));
        bean.setContactLastName(readRequiredUppercase("Cognome Referente: "));

        showMessage("\nInserimento Indirizzo Legale:");
        bean.setLegalAddress(readAddress("LEGAL"));

        if (readBoolean("L'indirizzo di fatturazione è diverso da quello legale?", false)) {
            showMessage("\nInserimento Indirizzo Fatturazione:");
            bean.setBillingAddress(readAddress(ADDRESS_TYPE_BILLING));
        } else {
            bean.setBillingAddress(null);
        }

        bean.setContacts(readContacts());
        return bean;
    }

    public RetailCompanyBean readUpdatedCompany(RetailCompanyBean current) {
        if (current == null) throw new IllegalArgumentException("Current retail company cannot be null");

        clearScreen();
        ui.showHeader();
        showTitle("Modifica Azienda", ui.theme().warning(), 80);
        showCompanyDetails(current, false);
        showMessage("Lascia vuoto per mantenere il valore attuale.\n");

        RetailCompanyBean updated = new RetailCompanyBean();
        updated.setVatNumber(current.getVatNumber());
        updated.setCompanyName(readUppercaseKeepingCurrent("Nome Azienda", current.getCompanyName()));
        updated.setContactFirstName(readUppercaseKeepingCurrent("Nome Referente", current.getContactFirstName()));
        updated.setContactLastName(readUppercaseKeepingCurrent("Cognome Referente", current.getContactLastName()));

        AddressBean legalAddress = copyAddress(current.getLegalAddress());
        if (readBoolean("Vuoi modificare l'indirizzo legale?", false)) {
            showMessage("\nModifica Indirizzo Legale:");
            legalAddress = readAddress("LEGAL");
        }
        updated.setLegalAddress(legalAddress);

        boolean separateBillingAddress = hasSeparateBillingAddress(current);
        if (separateBillingAddress) {
            AddressBean billingAddress = copyAddress(current.getBillingAddress());
            if (readBoolean("Vuoi modificare l'indirizzo di fatturazione?", false)) {
                showMessage("\nModifica Indirizzo Fatturazione:");
                billingAddress = readAddress(ADDRESS_TYPE_BILLING);
            }
            updated.setBillingAddress(billingAddress);
        } else if (readBoolean("Vuoi aggiungere un indirizzo di fatturazione diverso da quello legale?", false)) {
            showMessage("\nInserimento Indirizzo Fatturazione:");
            updated.setBillingAddress(readAddress(ADDRESS_TYPE_BILLING));
        } else {
            updated.setBillingAddress(null);
        }

        if (readBoolean("Vuoi modificare i contatti dell'azienda?", false)) updated.setContacts(readContacts());
        else updated.setContacts(copyContacts(current.getContacts()));

        return updated;
    }

    public String readVatNumber() {
        while (true) {
            String value = readRequiredString("Partita IVA (11 cifre numeriche): ").trim();
            RetailCompanyBean probe = new RetailCompanyBean();
            try {
                probe.setVatNumber(value);
                return probe.getVatNumber();
            } catch (IllegalArgumentException e) {
                showError("Partita IVA non valida. Deve essere composta da 11 cifre numeriche.");
            }
        }
    }

    public boolean confirmDelete(String vatNumber) {
        return readBoolean("Sei sicuro di voler eliminare l'azienda con Partita IVA " + text(vatNumber) + "?", false);
    }

    public boolean askShowCompanyDetails() {
        return readBoolean("Vuoi visualizzare i dettagli di un'azienda?", false);
    }

    public void showCompanyList(List<RetailCompanyBean> companies) {
        clearScreen();
        ui.showHeader();
        showTitle("Elenco Aziende Rivenditrici", ui.theme().primary(), 72);

        if (companies == null || companies.isEmpty()) {
            showMessage("Non ci sono aziende nel sistema.");
            waitForEnter();
            return;
        }

        List<String> headers = List.of("PARTITA IVA", "NOME AZIENDA", "REFERENTE");
        List<Integer> widths = List.of(13, 34, 28);
        List<List<String>> rows = new ArrayList<>();

        for (RetailCompanyBean company : companies) {
            rows.add(List.of(
                    text(company.getVatNumber()),
                    text(company.getCompanyName()),
                    fullContactName(company)
            ));
        }

        showTable(headers, rows, widths);
        showMessage("\nTotale aziende: " + companies.size());
    }

    public void showCompanyDetails(RetailCompanyBean company) {
        showCompanyDetails(company, true);
    }

    public void showCreateSuccess(String vatNumber) {
        clearScreen();
        ui.showHeader();
        showSuccess("Azienda con Partita IVA " + text(vatNumber) + " inserita con successo.");
    }

    public void showUpdateSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Azienda modificata con successo.");
    }

    public void showDeleteSuccess() {
        clearScreen();
        ui.showHeader();
        showSuccess("Azienda eliminata con successo.");
    }

    public void showDeleteCancelled() {
        clearScreen();
        ui.showHeader();
        showMessage("Eliminazione annullata.");
    }

    public void showCompanyNotFound(String vatNumber) {
        showError("Nessuna azienda trovata con Partita IVA: " + text(vatNumber));
    }

    private List<ContactBean> readContacts() {
        List<ContactBean> contacts = new ArrayList<>();
        boolean anotherContact;
        do {
            contacts.add(readContact());
            anotherContact = readBoolean("Vuoi inserire un altro contatto?", false);
        } while (anotherContact);
        return contacts;
    }

    private ContactBean readContact() {
        showTitle("Inserimento Nuovo Contatto", ui.theme().secondary(), 80);
        int choice = readMenuChoice("Seleziona il tipo di contatto", CONTACT_TYPE_LABELS);
        String type = CONTACT_TYPE_VALUES.get(choice - 1);

        while (true) {
            String value = readRequiredString(contactPrompt(type)).trim();
            ContactBean contact = new ContactBean();
            try {
                contact.setType(type);
                contact.setValue(value);
                return contact;
            } catch (IllegalArgumentException e) {
                showError(type.equals(CONTACT_TYPE_EMAIL)
                        ? "Email non valida."
                        : "Numero di telefono non valido. Inserire 8-15 cifre, opzionalmente con + iniziale.");
            }
        }
    }

    private AddressBean readAddress(String type) {
        AddressBean address = new AddressBean();
        address.setType(type);
        address.setStreet(readRequiredUppercase("Via e numero civico: "));
        address.setPostalCode(readPostalCode());
        address.setCity(readRequiredUppercase("Città: "));
        return address;
    }

    private String readPostalCode() {
        while (true) {
            String value = readRequiredString("CAP: ").trim();
            AddressBean probe = new AddressBean();
            try {
                probe.setPostalCode(value);
                return probe.getPostalCode();
            } catch (IllegalArgumentException e) {
                showError("Il CAP deve essere di 5 cifre numeriche.");
            }
        }
    }

    private void showCompanyDetails(RetailCompanyBean company, boolean clear) {
        if (company == null) {
            showWarning("Nessun dato azienda disponibile.");
            return;
        }

        if (clear) {
            clearScreen();
            ui.showHeader();
        }
        showTitle("Scheda Azienda", ui.theme().primary(), 80);

        showSeparator();
        showMessage("DATI PRINCIPALI:");
        showSeparator();
        showMessage(formatField("PARTITA IVA", company.getVatNumber()));
        showMessage(formatField("NOME AZIENDA", company.getCompanyName()));
        showMessage(formatField("REFERENTE", fullContactName(company)));

        showSeparator();
        showMessage("INDIRIZZO LEGALE:");
        showSeparator();
        showAddress(company.getLegalAddress());

        showSeparator();
        showMessage("INDIRIZZO FATTURAZIONE:");
        showSeparator();
        if (company.getBillingAddress() == null || !hasSeparateBillingAddress(company)) {
            showMessage("Coincide con l'indirizzo legale.");
        } else {
            showAddress(company.getBillingAddress());
        }

        showSeparator();
        showMessage("CONTATTI:");
        showSeparator();
        if (company.getContacts() == null || company.getContacts().isEmpty()) {
            showWarning("Nessun contatto registrato.");
        } else {
            for (ContactBean contact : company.getContacts()) {
                showMessage(formatField(contactTypeLabel(contact.getType()), contact.getValue()));
            }
        }
        showSeparator();
    }

    private void showAddress(AddressBean address) {
        if (address == null) {
            showWarning("Indirizzo non disponibile.");
            return;
        }
        showMessage(formatField("VIA", address.getStreet()));
        showMessage(formatField("CAP", address.getPostalCode()));
        showMessage(formatField("CITTÀ", address.getCity()));
    }

    private boolean hasSeparateBillingAddress(RetailCompanyBean company) {
        return company != null
                && company.getBillingAddress() != null
                && !sameAddressData(company.getLegalAddress(), company.getBillingAddress());
    }

    private boolean sameAddressData(AddressBean first, AddressBean second) {
        if (first == second) return true;
        if (first == null || second == null) return false;
        return normalized(first.getStreet()).equals(normalized(second.getStreet()))
                && normalized(first.getPostalCode()).equals(normalized(second.getPostalCode()))
                && normalized(first.getCity()).equals(normalized(second.getCity()));
    }

    private AddressBean copyAddress(AddressBean source) {
        if (source == null) return null;
        AddressBean copy = new AddressBean();
        copy.setId(source.getId());
        copy.setType(source.getType());
        copy.setStreet(source.getStreet());
        copy.setPostalCode(source.getPostalCode());
        copy.setCity(source.getCity());
        return copy;
    }

    private List<ContactBean> copyContacts(List<ContactBean> source) {
        List<ContactBean> copies = new ArrayList<>();
        if (source == null) return copies;
        for (ContactBean contact : source) {
            if (contact == null) continue;
            ContactBean copy = new ContactBean();
            copy.setType(contact.getType());
            copy.setValue(contact.getValue());
            copies.add(copy);
        }
        return copies;
    }

    private String readRequiredUppercase(String prompt) {
        return readRequiredString(prompt).toUpperCase(Locale.ROOT);
    }

    private String readUppercaseKeepingCurrent(String label, String currentValue) {
        String value = readString(label + " [" + text(currentValue) + "] (INVIO per mantenere): ");
        return value.isEmpty() ? currentValue : value.toUpperCase(Locale.ROOT);
    }

    private String fullContactName(RetailCompanyBean company) {
        String firstName = company == null ? "" : text(company.getContactFirstName());
        String lastName = company == null ? "" : text(company.getContactLastName());
        return (firstName + " " + lastName).trim();
    }

    private String contactPrompt(String type) {
        return switch (type) {
            case CONTACT_TYPE_PHONE -> "Inserisci il numero di telefono: ";
            case CONTACT_TYPE_MOBILE -> "Inserisci il numero di cellulare: ";
            case CONTACT_TYPE_EMAIL -> "Inserisci l'indirizzo email: ";
            default -> "Inserisci il contatto: ";
        };
    }

    private String contactTypeLabel(String type) {
        if (type == null || type.isBlank()) return "CONTATTO";
        return switch (type.toUpperCase(Locale.ROOT)) {
            case CONTACT_TYPE_PHONE, CONTACT_LABEL_PHONE -> CONTACT_LABEL_PHONE;
            case CONTACT_TYPE_MOBILE, CONTACT_LABEL_MOBILE -> CONTACT_LABEL_MOBILE;
            case CONTACT_TYPE_EMAIL -> CONTACT_TYPE_EMAIL;
            default -> type;
        };
    }

    private String formatField(String label, Object value) {
        return String.format("%-18s: %s", label, text(value));
    }

    private String normalized(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }

    private String text(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
