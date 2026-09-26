package it.verde.view;

import it.verde.view.bean.UserBean;
import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

import java.util.ArrayList;
import java.util.List;

public final class UserView extends BaseView {
    private static final String USER_PREFIX = "Utente '";
    private static final List<String> OPTIONS = List.of(
            "Inserisci Utente",
            "Visualizza Utenti",
            "Elimina Utente",
            "Torna al Menù Principale"
    );

    private static final List<String> ROLE_LABELS = List.of(
            "Responsabile Commerciale",
            "Responsabile Logistico"
    );

    private static final List<String> ROLE_VALUES = List.of(
            "SALES_MANAGER",
            "LOGISTICS_MANAGER"
    );

    public UserView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    public int showMenu() {
        clearScreen();
        ui.showHeader();
        showTitle("GESTIONE UTENTI", ui.theme().error(), 80);
        return readMenuChoice("Gestione Utenti", OPTIONS);
    }

    public UserBean readNewUser() {
        clearScreen();
        ui.showHeader();
        showTitle("INSERIMENTO UTENTE", ui.theme().success(), 80);

        UserBean bean = new UserBean();
        bean.setUsername(readUsernameForCreation());
        bean.setPassword(readPassword());
        bean.setRole(readRole());
        return bean;
    }

    public String readUsername() {
        return readRequiredString("Inserisci username: ").trim();
    }

    public boolean confirmDelete(String username) {
        clearScreen();
        ui.showHeader();
        showTitle("CONFERMA ELIMINAZIONE", ui.theme().error(), 80);
        showWarning("Stai per eliminare l'utente '" + text(username) + "'.");
        showWarning("Questa operazione non può essere annullata.");
        return readBoolean("Sei sicuro di voler procedere?", false);
    }

    public void showUsers(List<UserBean> users) {
        clearScreen();
        ui.showHeader();
        showTitle("ELENCO UTENTI", ui.theme().primary(), 64);

        if (users == null || users.isEmpty()) {
            showMessage("Nessun utente trovato.");
            waitForEnter();
            return;
        }

        List<String> headers = List.of("USERNAME", "RUOLO");
        List<Integer> widths = List.of(24, 34);
        List<List<String>> rows = new ArrayList<>();

        for (UserBean user : users) {
            rows.add(List.of(
                    text(user.getUsername()),
                    roleLabel(user.getRole())
            ));
        }

        showTable(headers, rows, widths);
        waitForEnter();
    }

    public void showCreateSuccess(String username) {
        clearScreen();
        ui.showHeader();
        showSuccess(USER_PREFIX + text(username) + "' creato con successo.");
    }

    public void showDeleteSuccess(String username) {
        clearScreen();
        ui.showHeader();
        showSuccess(USER_PREFIX + text(username) + "' eliminato con successo.");
    }

    public void showUsernameAlreadyExists(String username) {
        showError("Username '" + text(username) + "' già esistente.");
    }

    public void showUserNotFound(String username) {
        showWarning(USER_PREFIX + text(username) + "' non trovato.");
    }

    public void showDeleteCancelled() {
        showMessage("Eliminazione annullata.");
    }

    private String readUsernameForCreation() {
        while (true) {
            String value = readRequiredString("Username (3-20 caratteri, lettere, numeri e underscore): ").trim();
            UserBean probe = new UserBean();
            try {
                probe.setUsername(value);
                return probe.getUsername();
            } catch (IllegalArgumentException e) {
                showError("Username non valido. Deve contenere 3-20 caratteri tra lettere, numeri e underscore.");
            }
        }
    }

    private String readPassword() {
        while (true) {
            String value = readRequiredString("Password (minimo 8 caratteri, 1 maiuscola, 1 minuscola, 1 numero): ");
            UserBean probe = new UserBean();
            try {
                probe.setPassword(value);
                return probe.getPassword();
            } catch (IllegalArgumentException e) {
                showError("Password non valida. Deve contenere almeno 8 caratteri, una maiuscola, una minuscola e un numero.");
            }
        }
    }

    private String readRole() {
        int choice = readMenuChoice("Scegli il ruolo", ROLE_LABELS);
        return ROLE_VALUES.get(choice - 1);
    }

    private String roleLabel(String role) {
        if (role == null || role.isBlank()) return "-";
        return switch (role.trim().toUpperCase()) {
            case "ADMINISTRATOR" -> "Amministratore";
            case "SALES_MANAGER" -> "Responsabile Commerciale";
            case "LOGISTICS_MANAGER" -> "Responsabile Logistico";
            default -> role;
        };
    }

    private String text(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
