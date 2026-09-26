package it.verde.controller.factory;

import it.verde.controller.navigation.AdministratorController;
import it.verde.controller.navigation.RoleController;
import it.verde.model.type.UserRole;

public final class AdministratorControllerFactory extends RoleControllerFactory {
    @Override
    public UserRole supportedRole() {
        return UserRole.ADMINISTRATOR;
    }

    @Override
    public RoleController create() {
        return new AdministratorController();
    }
}
