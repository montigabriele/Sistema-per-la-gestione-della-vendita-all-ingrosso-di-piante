package it.verde.controller.ui;

import it.verde.controller.PriceController;
import it.verde.exception.ApplicationException;
import it.verde.view.PriceView;

import java.math.BigDecimal;

public final class PriceUiController implements UiController {
    private final PriceController controller;
    private final PriceView view;

    public PriceUiController(PriceController controller, PriceView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            switch (view.showMenu()) {
                case 1 -> execute(() -> {
                    String code = view.readSpeciesCode();
                    BigDecimal price = view.readNewPrice();
                    controller.changePrice(code, price);
                    view.showPriceChangeSuccess(code, price);
                    view.waitForEnter();
                });
                case 2 -> execute(() -> view.showPriceHistory(controller.getPriceHistory(view.readSpeciesCode())));
                case 3 -> running = false;
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
