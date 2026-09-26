package it.verde;

import it.verde.controller.ApplicationController;
import it.verde.controller.navigation.NavigationTarget;
import it.verde.controller.navigation.RoleController;
import it.verde.controller.ui.UiController;
import it.verde.exception.ApplicationException;
import it.verde.view.AdministratorView;
import it.verde.view.ApplicationView;
import it.verde.view.LoginView;
import it.verde.view.LogisticsManagerView;
import it.verde.view.SalesManagerView;
import it.verde.view.bean.LoginResultBean;

public final class ApplicationRunner {
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int MAX_DATABASE_ERRORS = 3;
    private static final long DATABASE_RETRY_DELAY_MILLIS = 10_000L;

    private final ApplicationController applicationController;
    private final ApplicationView applicationView;
    private final LoginView loginView;
    private final AdministratorView administratorView;
    private final SalesManagerView salesManagerView;
    private final LogisticsManagerView logisticsManagerView;
    private final UiController plantSpeciesUiController;
    private final UiController priceUiController;
    private final UiController retailCompanyUiController;
    private final UiController salesOrderUiController;
    private final UiController warehouseUiController;
    private final UiController supplyOrderUiController;
    private final UiController supplierUiController;
    private final UiController userUiController;

    public record Views(
            ApplicationView applicationView,
            LoginView loginView,
            AdministratorView administratorView,
            SalesManagerView salesManagerView,
            LogisticsManagerView logisticsManagerView) {
    }

    public record AdministratorUiControllers(UiController userUiController) {
    }

    public record SalesUiControllers(
            UiController plantSpeciesUiController,
            UiController priceUiController,
            UiController retailCompanyUiController,
            UiController salesOrderUiController) {
    }

    public record LogisticsUiControllers(
            UiController warehouseUiController,
            UiController supplyOrderUiController,
            UiController supplierUiController) {
    }

    public ApplicationRunner(
            ApplicationController applicationController,
            Views views,
            AdministratorUiControllers administratorUiControllers,
            SalesUiControllers salesUiControllers,
            LogisticsUiControllers logisticsUiControllers) {
        this.applicationController = applicationController;
        this.applicationView = views.applicationView();
        this.loginView = views.loginView();
        this.administratorView = views.administratorView();
        this.salesManagerView = views.salesManagerView();
        this.logisticsManagerView = views.logisticsManagerView();
        this.userUiController = administratorUiControllers.userUiController();
        this.plantSpeciesUiController = salesUiControllers.plantSpeciesUiController();
        this.priceUiController = salesUiControllers.priceUiController();
        this.retailCompanyUiController = salesUiControllers.retailCompanyUiController();
        this.salesOrderUiController = salesUiControllers.salesOrderUiController();
        this.warehouseUiController = logisticsUiControllers.warehouseUiController();
        this.supplyOrderUiController = logisticsUiControllers.supplyOrderUiController();
        this.supplierUiController = logisticsUiControllers.supplierUiController();
    }

    public void run() {
        try {
            ensureDatabaseAvailable();

            LoginResultBean loginResult = authenticate();
            RoleController roleController = applicationController.resolveRoleController(loginResult);

            applicationView.showApplicationHeader();
            applicationView.showSuccess("Login effettuato con successo.");
            applicationView.showMessage("Benvenuto: " + loginResult.getUsername() + ".");
            applicationView.waitForEnter();

            runRoleMenu(roleController);
        } catch (ApplicationException e) {
            applicationView.showError(e.getMessage());
        } finally {
            applicationView.showMessage("\nUscita dal sistema...\nArrivederci!\n");
        }
    }

    private void ensureDatabaseAvailable() throws ApplicationException {
        ApplicationException lastError = null;

        for (int attempt = 1; attempt <= MAX_DATABASE_ERRORS; attempt++) {
            try {
                if (applicationController.isDatabaseAvailable()) return;
                lastError = new ApplicationException("Connessione al database non disponibile.");
            } catch (ApplicationException e) {
                lastError = e;
            }

            if (attempt < MAX_DATABASE_ERRORS) {
                try {
                    Thread.sleep(DATABASE_RETRY_DELAY_MILLIS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ApplicationException("Operazione interrotta durante l'attesa di riconnessione al database.", e);
                }
            }
        }

        throw new ApplicationException("Connessione al database persa. L'applicazione verrà chiusa.", lastError);
    }

    private LoginResultBean authenticate() throws ApplicationException {
        for (int attempt = 1; attempt <= MAX_LOGIN_ATTEMPTS; attempt++) {
            LoginResultBean result = applicationController.authenticate(loginView.readCredentials());
            if (result.isSuccessful()) return result;

            String message = result.getErrorMessage();
            loginView.showError(message == null || message.isBlank() ? "Credenziali non valide." : message);
            int remaining = MAX_LOGIN_ATTEMPTS - attempt;
            if (remaining > 0) {
                loginView.showWarning("Tentativi rimanenti: " + remaining);
                loginView.waitForEnter();
            }
        }
        throw new ApplicationException("Troppi tentativi di autenticazione falliti.");
    }

    private void runRoleMenu(RoleController roleController) throws ApplicationException {
        while (true) {
            int option = switch (roleController.getRole()) {
                case ADMINISTRATOR -> administratorView.showMainMenu();
                case SALES_MANAGER -> salesManagerView.showMainMenu();
                case LOGISTICS_MANAGER -> logisticsManagerView.showMainMenu();
            };

            NavigationTarget target = roleController.selectOption(option);
            if (target == NavigationTarget.LOGOUT) return;
            openTarget(target);
        }
    }

    private void openTarget(NavigationTarget target) {
        switch (target) {
            case USERS -> userUiController.run();
            case PLANT_SPECIES -> plantSpeciesUiController.run();
            case PRICES -> priceUiController.run();
            case SALES_ORDERS -> salesOrderUiController.run();
            case RETAIL_COMPANIES -> retailCompanyUiController.run();
            case WAREHOUSE -> warehouseUiController.run();
            case SUPPLY_ORDERS -> supplyOrderUiController.run();
            case SUPPLIERS -> supplierUiController.run();
            case LOGOUT -> {
                // Intentionally empty: logout is handled by runRoleMenu before dispatching a target.
            }
        }
    }
}
