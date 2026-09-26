package it.verde.controller.ui;

import it.verde.controller.SalesOrderController;
import it.verde.exception.ApplicationException;
import it.verde.view.SalesOrderView;
import it.verde.view.bean.SalesOrderBean;
import it.verde.view.bean.SalesOrderItemBean;

import java.util.List;
import java.util.Optional;

public final class SalesOrderUiController implements UiController {
    private final SalesOrderController controller;
    private final SalesOrderView view;

    public SalesOrderUiController(SalesOrderController controller, SalesOrderView view) {
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
        SalesOrderBean order = view.readNewOrder();
        boolean another;
        do {
            SalesOrderItemBean requested = view.readOrderItem();
            int available = controller.getAvailableQuantity(requested.getSpeciesCode());
            view.showAvailabilityRequest(requested.getSpeciesCode(), available, requested.getQuantity());
            if (available >= requested.getQuantity()) {
                order.getItems().add(controller.createOrderItem(requested.getSpeciesCode(), requested.getQuantity()));
                another = view.askAddAnotherItem();
            } else {
                view.showWarning("Quantità non disponibile. Inserire un altro articolo o una quantità inferiore.");
                another = true;
            }
        } while (another);

        view.showCreateSuccess(controller.createOrder(order));
        view.waitForEnter();
    }

    private void showAll() throws ApplicationException {
        List<SalesOrderBean> orders = controller.getAllOrders();
        view.showOrders(orders);
        if (!orders.isEmpty() && view.askShowOrderDetails()) showDetails();
        else view.waitForEnter();
    }

    private void showDetails() throws ApplicationException {
        int orderId = view.readOrderId();
        Optional<SalesOrderBean> order = controller.getOrderById(orderId);
        if (order.isPresent()) view.showOrderDetails(order.get());
        else {
            view.showOrderNotFound(orderId);
            view.waitForEnter();
        }
    }

    private void update() throws ApplicationException {
        List<SalesOrderBean> orders = controller.getAllOrders();
        view.showOrders(orders);
        if (orders.isEmpty()) {
            view.waitForEnter();
            return;
        }
        int orderId = view.readOrderId();
        Optional<SalesOrderBean> current = controller.getOrderById(orderId);
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
        List<SalesOrderBean> orders = controller.getAllOrders();
        view.showOrders(orders);
        if (orders.isEmpty()) {
            view.waitForEnter();
            return;
        }
        int orderId = view.readOrderId();
        Optional<SalesOrderBean> order = controller.getOrderById(orderId);
        if (order.isEmpty()) {
            view.showOrderNotFound(orderId);
            view.waitForEnter();
            return;
        }
        view.showOrderDetails(order.get());
        if (view.confirmDelete(orderId)) {
            if (controller.deleteOrder(orderId)) view.showDeleteSuccess();
            else view.showError("Impossibile eliminare l'ordine.");
        } else {
            view.showMessage("Eliminazione annullata.");
        }
        view.waitForEnter();
    }

    private void changeStatus() throws ApplicationException {
        List<SalesOrderBean> orders = controller.getAllOrders();
        view.showOrders(orders);
        if (orders.isEmpty()) {
            view.waitForEnter();
            return;
        }
        int orderId = view.readOrderId();
        Optional<SalesOrderBean> order = controller.getOrderById(orderId);
        if (order.isEmpty()) {
            view.showOrderNotFound(orderId);
            view.waitForEnter();
            return;
        }
        view.showOrderDetails(order.get());
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
                case 1 -> execute(this::showDetails);
                case 2 -> execute(() -> {
                    String status = view.readStatus();
                    view.showOrdersByStatus(controller.getOrdersByStatus(status), status);
                });
                case 3 -> execute(() -> {
                    String vatNumber = view.readCustomerVatNumber();
                    view.showOrdersByCustomer(controller.getOrdersByCustomerVatNumber(vatNumber), vatNumber);
                });
                case 4 -> running = false;
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
