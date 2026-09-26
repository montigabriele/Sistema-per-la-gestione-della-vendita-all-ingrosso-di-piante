package it.verde.controller.ui;

import it.verde.controller.UserController;
import it.verde.exception.ApplicationException;
import it.verde.view.UserView;
import it.verde.view.bean.UserBean;

public final class UserUiController implements UiController {
    private final UserController controller;
    private final UserView view;

    public UserUiController(UserController controller, UserView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            switch (view.showMenu()) {
                case 1 -> execute(this::create);
                case 2 -> execute(() -> view.showUsers(controller.getAllUsers()));
                case 3 -> execute(this::delete);
                case 4 -> running = false;
                default -> view.showError("Scelta non valida.");
            }
        }
    }

    private void create() throws ApplicationException {
        UserBean user = view.readNewUser();
        if (controller.createUser(user)) view.showCreateSuccess(user.getUsername());
        else view.showUsernameAlreadyExists(user.getUsername());
        view.waitForEnter();
    }

    private void delete() throws ApplicationException {
        String username = view.readUsername();
        if (!view.confirmDelete(username)) {
            view.showDeleteCancelled();
            view.waitForEnter();
            return;
        }
        if (controller.deleteUser(username)) view.showDeleteSuccess(username);
        else view.showUserNotFound(username);
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
