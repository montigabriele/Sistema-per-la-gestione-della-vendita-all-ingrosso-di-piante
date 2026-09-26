package it.verde.controller.navigation;

import it.verde.exception.ApplicationException;
import it.verde.model.type.UserRole;

public interface RoleController {
    UserRole getRole();
    NavigationTarget selectOption(int option) throws ApplicationException;
}
