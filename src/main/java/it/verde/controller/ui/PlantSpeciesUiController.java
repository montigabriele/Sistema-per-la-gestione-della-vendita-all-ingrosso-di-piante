package it.verde.controller.ui;

import it.verde.controller.PlantSpeciesController;
import it.verde.exception.ApplicationException;
import it.verde.view.PlantSpeciesView;
import it.verde.view.bean.PlantSpeciesBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public final class PlantSpeciesUiController implements UiController {
    private final PlantSpeciesController controller;
    private final PlantSpeciesView view;

    public PlantSpeciesUiController(PlantSpeciesController controller, PlantSpeciesView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            switch (view.showMenu()) {
                case 1 -> execute(this::create);
                case 2 -> execute(() -> view.showSpeciesList(controller.getAllSpecies()));
                case 3 -> execute(() -> view.showCatalog(controller.getCatalog()));
                case 4 -> execute(() -> view.showSalesReport(controller.getSalesReport()));
                case 5 -> execute(this::update);
                case 6 -> execute(this::delete);
                case 7 -> runSearch();
                case 8 -> running = false;
                default -> view.showError("Scelta non valida.");
            }
        }
    }

    private void create() throws ApplicationException {
        PlantSpeciesBean bean = view.readNewSpecies();
        BigDecimal initialPrice = view.readInitialPrice();
        view.showCreateSuccess(controller.createSpecies(bean, initialPrice));
        view.waitForEnter();
    }

    private void update() throws ApplicationException {
        String code = view.readSpeciesCode();
        Optional<PlantSpeciesBean> current = controller.getSpeciesByCode(code);
        if (current.isEmpty()) {
            view.showError("Specie non trovata.");
            view.waitForEnter();
            return;
        }
        controller.updateSpecies(view.readUpdatedSpecies(current.get()));
        view.showUpdateSuccess();
        view.waitForEnter();
    }

    private void delete() throws ApplicationException {
        if (controller.deleteSpecies(view.readSpeciesCode())) view.showDeleteSuccess();
        else view.showError("Specie non trovata.");
        view.waitForEnter();
    }

    private void runSearch() {
        boolean running = true;
        while (running) {
            switch (view.showSearchMenu()) {
                case 1 -> execute(this::searchByName);
                case 2 -> execute(this::searchByPlantType);
                case 3 -> execute(this::searchByFlowerColor);
                case 4 -> running = false;
                default -> view.showError("Scelta non valida.");
            }
        }
    }

    private void searchByName() throws ApplicationException {
        String term = view.readSearchTerm();
        List<PlantSpeciesBean> result = controller.searchByName(term);
        if (result.isEmpty()) view.showNoSearchResults("con il termine: " + term);
        else view.showSpeciesList(result);
    }

    private void searchByPlantType() throws ApplicationException {
        String type = view.readPlantType();
        List<PlantSpeciesBean> result = controller.searchByPlantType(type);
        if (result.isEmpty()) view.showNoSearchResults("per la tipologia selezionata");
        else view.showSpeciesList(result);
    }

    private void searchByFlowerColor() throws ApplicationException {
        String color = view.readFlowerColor();
        List<PlantSpeciesBean> result = controller.searchByFlowerColor(color);
        if (result.isEmpty()) view.showNoSearchResults("per la colorazione selezionata");
        else view.showSpeciesList(result);
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
