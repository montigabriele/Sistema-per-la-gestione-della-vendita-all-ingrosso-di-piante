package it.verde.controller.ui;

import it.verde.controller.WarehouseController;
import it.verde.exception.ApplicationException;
import it.verde.view.WarehouseView;
import it.verde.view.bean.WarehouseStockBean;

public final class WarehouseUiController implements UiController {
    private final WarehouseController controller;
    private final WarehouseView view;

    public WarehouseUiController(WarehouseController controller, WarehouseView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            switch (view.showMenu()) {
                case 1 -> execute(() -> {
                    controller.createStock(view.readNewStock());
                    view.showCreateSuccess();
                    view.waitForEnter();
                });
                case 2 -> execute(() -> view.showWarehouseOverview(controller.getWarehouseOverview()));
                case 3 -> execute(() -> view.showCriticalStocks(controller.getCriticalStocks()));
                case 4 -> execute(() -> {
                    String code = view.readSpeciesCode();
                    int current = controller.getQuantity(code);
                    WarehouseStockBean updated = view.readUpdatedStock(code, current);
                    controller.updateStock(updated);
                    view.showUpdateSuccess();
                    view.waitForEnter();
                });
                case 5 -> running = false;
                default -> view.showError("Scelta non valida.");
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
