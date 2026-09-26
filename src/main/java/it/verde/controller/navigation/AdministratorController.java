package it.verde.controller.navigation;

import it.verde.exception.ApplicationException;
import it.verde.model.type.UserRole;

public final class AdministratorController implements RoleController {
    @Override
    public UserRole getRole() {
        return UserRole.ADMINISTRATOR;
    }

    @Override
    public NavigationTarget selectOption(int option) throws ApplicationException {
        return switch (option) {
            case 1 -> NavigationTarget.USERS;
            case 2 -> NavigationTarget.LOGOUT;
            default -> throw new ApplicationException("Invalid administrator menu option: " + option);
        };
    }
}
