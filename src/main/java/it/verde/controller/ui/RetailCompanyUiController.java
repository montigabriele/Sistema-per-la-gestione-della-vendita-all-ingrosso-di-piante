package it.verde.controller.ui;

import it.verde.controller.RetailCompanyController;
import it.verde.exception.ApplicationException;
import it.verde.view.RetailCompanyView;
import it.verde.view.bean.RetailCompanyBean;

import java.util.List;
import java.util.Optional;

public final class RetailCompanyUiController implements UiController {
    private final RetailCompanyController controller;
    private final RetailCompanyView view;

    public RetailCompanyUiController(RetailCompanyController controller, RetailCompanyView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            switch (view.showMenu()) {
                case 1 -> execute(this::create);
                case 2 -> execute(this::showAll);
                case 3 -> execute(this::update);
                case 4 -> execute(this::delete);
                case 5 -> execute(this::showDetails);
                case 6 -> running = false;
                default -> view.showError("Scelta non valida.");
            }
        }
    }

    private void create() throws ApplicationException {
        RetailCompanyBean bean = view.readNewCompany();
        view.showCreateSuccess(controller.createCompany(bean));
        view.waitForEnter();
    }

    private void showAll() throws ApplicationException {
        List<RetailCompanyBean> companies = controller.getAllCompanies();
        view.showCompanyList(companies);
        if (!companies.isEmpty() && view.askShowCompanyDetails()) showDetails();
        else view.waitForEnter();
    }

    private void showDetails() throws ApplicationException {
        String vatNumber = view.readVatNumber();
        Optional<RetailCompanyBean> company = controller.getCompanyByVatNumber(vatNumber);
        if (company.isPresent()) view.showCompanyDetails(company.get());
        else view.showCompanyNotFound(vatNumber);
        view.waitForEnter();
    }

    private void update() throws ApplicationException {
        String vatNumber = view.readVatNumber();
        Optional<RetailCompanyBean> current = controller.getCompanyByVatNumber(vatNumber);
        if (current.isEmpty()) {
            view.showCompanyNotFound(vatNumber);
            view.waitForEnter();
            return;
        }
        controller.updateCompany(view.readUpdatedCompany(current.get()));
        view.showUpdateSuccess();
        view.waitForEnter();
    }

    private void delete() throws ApplicationException {
        String vatNumber = view.readVatNumber();
        if (!controller.existsByVatNumber(vatNumber)) {
            view.showCompanyNotFound(vatNumber);
            view.waitForEnter();
            return;
        }
        if (!view.confirmDelete(vatNumber)) {
            view.showDeleteCancelled();
            view.waitForEnter();
            return;
        }
        if (controller.deleteCompany(vatNumber)) view.showDeleteSuccess();
        else view.showCompanyNotFound(vatNumber);
        view.waitForEnter();
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
