package it.verde.controller.ui;

import it.verde.controller.SupplierController;
import it.verde.exception.ApplicationException;
import it.verde.view.SupplierView;
import it.verde.view.bean.SupplierBean;

import java.util.Optional;

public final class SupplierUiController implements UiController {
    private static final String INVALID_CHOICE_MESSAGE = "Scelta non valida.";

    private final SupplierController controller;
    private final SupplierView view;

    public SupplierUiController(SupplierController controller, SupplierView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            switch (view.showMenu()) {
                case 1 -> execute(() -> {
                    int supplierId = controller.createSupplier(view.readNewSupplier());
                    view.showCreateSuccess(supplierId);
                    view.waitForEnter();
                });
                case 2 -> runViewMenu();
                case 3 -> runUpdateMenu();
                case 4 -> execute(this::delete);
                case 5 -> runSupplyMenu();
                case 6 -> running = false;
                default -> view.showError(INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void runViewMenu() {
        boolean running = true;
        while (running) {
            switch (view.showViewMenu()) {
                case 1 -> execute(() -> {
                    view.showSuppliers(controller.getAllSuppliers());
                    view.waitForEnter();
                });
                case 2 -> execute(() -> {
                    int supplierId = view.readSupplierId();
                    Optional<SupplierBean> supplier = controller.getSupplierById(supplierId);
                    if (supplier.isPresent()) view.showSupplierDetails(supplier.get());
                    else view.showSupplierNotFound(supplierId);
                    view.waitForEnter();
                });
                case 3 -> running = false;
                default -> view.showError(INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void runUpdateMenu() {
        boolean running = true;
        while (running) {
            switch (view.showUpdateMenu()) {
                case 1 -> execute(() -> {
                    int supplierId = view.readSupplierId();
                    Optional<SupplierBean> current = controller.getSupplierById(supplierId);
                    if (current.isEmpty()) {
                        view.showSupplierNotFound(supplierId);
                        view.waitForEnter();
                        return;
                    }
                    controller.updateSupplier(view.readUpdatedSupplier(current.get()));
                    view.showUpdateSuccess();
                    view.waitForEnter();
                });
                case 2 -> execute(() -> {
                    int supplierId = view.readSupplierId();
                    Optional<SupplierBean> current = controller.getSupplierById(supplierId);
                    if (current.isEmpty()) {
                        view.showSupplierNotFound(supplierId);
                        view.waitForEnter();
                        return;
                    }
                    SupplierBean updated = current.get();
                    updated.setAddresses(view.readUpdatedAddresses(updated.getAddresses()));
                    controller.updateSupplier(updated);
                    view.showUpdateSuccess();
                    view.waitForEnter();
                });
                case 3 -> running = false;
                default -> view.showError(INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void delete() throws ApplicationException {
        int supplierId = view.readSupplierId();
        if (!controller.existsById(supplierId)) {
            view.showSupplierNotFound(supplierId);
            view.waitForEnter();
            return;
        }
        if (view.confirmDelete(supplierId)) {
            if (controller.deleteSupplier(supplierId)) view.showDeleteSuccess();
            else view.showSupplierNotFound(supplierId);
        } else view.showMessage("Eliminazione annullata.");
        view.waitForEnter();
    }

    private void runSupplyMenu() {
        boolean running = true;
        while (running) {
            switch (view.showSupplyMenu()) {
                case 1 -> execute(() -> {
                    int supplierId = view.readSupplierId();
                    String speciesCode = view.readSpeciesCode();
                    if (view.confirmAddSupply(supplierId, speciesCode)) {
                        controller.addSuppliedSpecies(supplierId, speciesCode);
                        view.showSupplyAddedSuccess();
                    }
                    view.waitForEnter();
                });
                case 2 -> execute(() -> {
                    int supplierId = view.readSupplierId();
                    String speciesCode = view.readSpeciesCode();
                    if (view.confirmRemoveSupply(supplierId, speciesCode)) {
                        controller.removeSuppliedSpecies(supplierId, speciesCode);
                        view.showSupplyRemovedSuccess();
                    }
                    view.waitForEnter();
                });
                case 3 -> execute(() -> {
                    int supplierId = view.readSupplierId();
                    view.showSuppliedSpecies(controller.getSuppliedSpecies(supplierId), supplierId);
                    view.waitForEnter();
                });
                case 4 -> execute(() -> {
                    int supplierId = view.readSupplierId();
                    String speciesCode = view.readSpeciesCode();
                    view.showSupplyCheckResult(supplierId, speciesCode, controller.suppliesSpecies(supplierId, speciesCode));
                    view.waitForEnter();
                });
                case 5 -> running = false;
                default -> view.showError(INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void execute(Action action) {
        try {
            action.run();
        } catch (ApplicationException | IllegalArgumentException e) {
            view.showError(e.getMessage());
            view.waitForEnter();
        }
    }

    @FunctionalInterface
    private interface Action {
        void run() throws ApplicationException;
    }
}
