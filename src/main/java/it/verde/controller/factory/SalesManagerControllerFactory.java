package it.verde.controller.factory;

import it.verde.controller.navigation.RoleController;
import it.verde.controller.navigation.SalesManagerController;
import it.verde.model.type.UserRole;

public final class SalesManagerControllerFactory extends RoleControllerFactory {
    @Override
    public UserRole supportedRole() {
        return UserRole.SALES_MANAGER;
    }

    @Override
    public RoleController create() {
        return new SalesManagerController();
    }
}
