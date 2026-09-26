package it.verde.controller.navigation;

import it.verde.exception.ApplicationException;
import it.verde.model.type.UserRole;

public final class LogisticsManagerController implements RoleController {
    @Override
    public UserRole getRole() {
        return UserRole.LOGISTICS_MANAGER;
    }

    @Override
    public NavigationTarget selectOption(int option) throws ApplicationException {
        return switch (option) {
            case 1 -> NavigationTarget.WAREHOUSE;
            case 2 -> NavigationTarget.SUPPLY_ORDERS;
            case 3 -> NavigationTarget.SUPPLIERS;
            case 4 -> NavigationTarget.LOGOUT;
            default -> throw new ApplicationException("Invalid logistics manager menu option: " + option);
        };
    }
}
