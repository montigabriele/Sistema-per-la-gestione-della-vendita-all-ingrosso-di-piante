package it.verde.controller.factory;

import it.verde.controller.navigation.LogisticsManagerController;
import it.verde.controller.navigation.RoleController;
import it.verde.model.type.UserRole;

public final class LogisticsManagerControllerFactory extends RoleControllerFactory {
    @Override
    public UserRole supportedRole() {
        return UserRole.LOGISTICS_MANAGER;
    }

    @Override
    public RoleController create() {
        return new LogisticsManagerController();
    }
}
