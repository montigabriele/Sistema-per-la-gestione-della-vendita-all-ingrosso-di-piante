package it.verde.controller.factory;

import it.verde.controller.navigation.RoleController;
import it.verde.model.type.UserRole;

public abstract class RoleControllerFactory {
    public abstract UserRole supportedRole();
    public abstract RoleController create();
}
