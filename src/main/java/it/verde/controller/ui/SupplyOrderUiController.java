package it.verde.controller.ui;

import it.verde.controller.SupplyOrderController;
import it.verde.exception.ApplicationException;
import it.verde.view.SupplyOrderView;
import it.verde.view.bean.PlantSpeciesBean;
import it.verde.view.bean.SupplierBean;
import it.verde.view.bean.SupplyOrderBean;
import it.verde.view.bean.SupplyOrderItemBean;

import java.util.List;
import java.util.Optional;

public final class SupplyOrderUiController implements UiController {
    private final SupplyOrderController controller;
    private final SupplyOrderView view;

    public SupplyOrderUiController(SupplyOrderController controller, SupplyOrderView view) {
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
                case 5 -> execute(this::changeStatus);
                case 6 -> runSearch();
                case 7 -> running = false;
                default -> view.showError("Scelta non valida.");
            }
        }
    }

    private void create() throws ApplicationException {
        List<SupplierBean> suppliers = controller.getAvailableSuppliers();
        view.showSuppliers(suppliers);
        if (suppliers.isEmpty()) {
            view.waitForEnter();
            return;
        }

        int supplierId = view.readSupplierId();
        Optional<SupplierBean> supplier = controller.getSupplierById(supplierId);
        if (supplier.isEmpty()) {
            view.showSupplierNotFound(supplierId);
            view.waitForEnter();
            return;
        }

        List<PlantSpeciesBean> suppliedSpecies = controller.getSuppliedSpecies(supplierId);
        view.showSuppliedSpecies(suppliedSpecies, supplierId);
        if (suppliedSpecies.isEmpty()) {
            view.waitForEnter();
            return;
        }

        SupplyOrderBean order = view.readNewOrder(supplierId);
        boolean another;
        do {
            SupplyOrderItemBean requested = view.readOrderItem();
            SupplyOrderItemBean item = controller.createOrderItem(supplierId, requested.getSpeciesCode(), requested.getQuantity());
            view.showStockInfo(item.getSpeciesCode(), controller.getAvailableQuantity(item.getSpeciesCode()));
            if (view.confirmItem(item)) {
                order.getItems().add(item);
                view.showItemAdded(item);
            }
            another = view.askAddAnotherItem();
        } while (another || order.getItems().isEmpty());

        if (!view.confirmCreation(order)) {
            view.showMessage("Creazione ordine annullata.");
            view.waitForEnter();
            return;
        }

        view.showCreateSuccess(controller.createOrder(order));
        view.waitForEnter();
    }

    private void showAll() throws ApplicationException {
        List<SupplyOrderBean> orders = controller.getAllOrders();
        view.showOrders(orders);
        if (!orders.isEmpty() && view.askShowOrderDetails()) showDetails();
        else view.waitForEnter();
    }

    private void showDetails() throws ApplicationException {
        int orderId = view.readOrderId();
        Optional<SupplyOrderBean> order = controller.getOrderById(orderId);
        if (order.isEmpty()) {
            view.showOrderNotFound(orderId);
            view.waitForEnter();
            return;
        }
        SupplierBean supplier = controller.getSupplierById(order.get().getSupplierId()).orElse(null);
        view.showOrderDetails(order.get(), supplier);
    }

    private void update() throws ApplicationException {
        List<SupplyOrderBean> orders = controller.getAllOrders();
        view.showOrders(orders);
        if (orders.isEmpty()) {
            view.waitForEnter();
            return;
        }
        int orderId = view.readOrderId();
        Optional<SupplyOrderBean> current = controller.getOrderById(orderId);
        if (current.isEmpty()) {
            view.showOrderNotFound(orderId);
            view.waitForEnter();
            return;
        }
        controller.updateOrder(view.readUpdatedOrder(current.get()));
        view.showUpdateSuccess();
        view.waitForEnter();
    }

    private void delete() throws ApplicationException {
        List<SupplyOrderBean> orders = controller.getAllOrders();
        view.showOrders(orders);
        if (orders.isEmpty()) {
            view.waitForEnter();
            return;
        }
        int orderId = view.readOrderId();
        Optional<SupplyOrderBean> order = controller.getOrderById(orderId);
        if (order.isEmpty()) {
            view.showOrderNotFound(orderId);
            view.waitForEnter();
            return;
        }
        SupplierBean supplier = controller.getSupplierById(order.get().getSupplierId()).orElse(null);
        view.showOrderDetails(order.get(), supplier);
        if (view.confirmDelete(orderId)) {
            if (controller.deleteOrder(orderId)) view.showDeleteSuccess();
            else view.showError("Impossibile eliminare l'ordine di rifornimento.");
        } else view.showMessage("Eliminazione annullata.");
        view.waitForEnter();
    }

    private void changeStatus() throws ApplicationException {
        List<SupplyOrderBean> orders = controller.getAllOrders();
        view.showOrders(orders);
        if (orders.isEmpty()) {
            view.waitForEnter();
            return;
        }
        int orderId = view.readOrderId();
        Optional<SupplyOrderBean> order = controller.getOrderById(orderId);
        if (order.isEmpty()) {
            view.showOrderNotFound(orderId);
            view.waitForEnter();
            return;
        }
        String newStatus = view.readNewStatus();
        if (newStatus.equalsIgnoreCase(order.get().getStatus())) view.showMessage("Lo stato è invariato.");
        else if (view.confirmStatusChange(order.get(), newStatus)) {
            controller.changeOrderStatus(orderId, newStatus);
            view.showStatusChangeSuccess();
        } else view.showMessage("Operazione annullata.");
        view.waitForEnter();
    }

    private void runSearch() {
        boolean running = true;
        while (running) {
            switch (view.showSearchMenu()) {
                case 1 -> execute(() -> {
                    String status = view.readStatus();
                    view.showOrdersByStatus(controller.getOrdersByStatus(status), status);
                });
                case 2 -> execute(() -> {
                    int supplierId = view.readSupplierId();
                    Optional<SupplierBean> supplier = controller.getSupplierById(supplierId);
                    if (supplier.isEmpty()) {
                        view.showSupplierNotFound(supplierId);
                        view.waitForEnter();
                        return;
                    }
                    view.showOrdersBySupplier(controller.getOrdersBySupplier(supplierId), supplier.get());
                });
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
