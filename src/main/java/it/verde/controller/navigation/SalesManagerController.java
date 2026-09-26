package it.verde.controller.navigation;

import it.verde.exception.ApplicationException;
import it.verde.model.type.UserRole;

public final class SalesManagerController implements RoleController {
    @Override
    public UserRole getRole() {
        return UserRole.SALES_MANAGER;
    }

    @Override
    public NavigationTarget selectOption(int option) throws ApplicationException {
        return switch (option) {
            case 1 -> NavigationTarget.PLANT_SPECIES;
            case 2 -> NavigationTarget.PRICES;
            case 3 -> NavigationTarget.SALES_ORDERS;
            case 4 -> NavigationTarget.RETAIL_COMPANIES;
            case 5 -> NavigationTarget.LOGOUT;
            default -> throw new ApplicationException("Invalid sales manager menu option: " + option);
        };
    }
}
