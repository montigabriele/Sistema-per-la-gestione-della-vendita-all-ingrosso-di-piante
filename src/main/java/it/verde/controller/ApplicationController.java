package it.verde.controller;

import it.verde.controller.factory.RoleControllerRouter;
import it.verde.controller.navigation.RoleController;
import it.verde.exception.ApplicationException;
import it.verde.exception.DaoException;
import it.verde.model.type.UserRole;
import it.verde.persistence.connection.DatabaseSession;
import it.verde.view.bean.LoginCredentialsBean;
import it.verde.view.bean.LoginResultBean;

import java.util.Objects;

public final class ApplicationController {
    private final AuthenticationController authenticationController;
    private final RoleControllerRouter roleControllerRouter;
    private final DatabaseSession databaseSession;

    public ApplicationController(AuthenticationController authenticationController, RoleControllerRouter roleControllerRouter, DatabaseSession databaseSession) {
        this.authenticationController = Objects.requireNonNull(authenticationController);
        this.roleControllerRouter = Objects.requireNonNull(roleControllerRouter);
        this.databaseSession = Objects.requireNonNull(databaseSession);
    }

    public LoginResultBean authenticate(LoginCredentialsBean credentials) throws ApplicationException {
        try {
            databaseSession.useLoginRole();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to prepare database login session", e);
        }
        return authenticationController.authenticate(credentials);
    }

    public RoleController resolveRoleController(LoginResultBean loginResult) throws ApplicationException {
        if (loginResult == null) throw new ApplicationException("Login result is required");
        if (!loginResult.isSuccessful()) throw new ApplicationException(resolveAuthenticationError(loginResult));

        UserRole role = parseRole(loginResult.getRole());
        RoleController controller = roleControllerRouter.resolve(role);

        try {
            databaseSession.useRole(role);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to activate database permissions for authenticated role", e);
        }

        return controller;
    }

    public boolean isDatabaseAvailable() throws ApplicationException {
        try {
            return databaseSession.isValid(5);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify database connection", e);
        }
    }

    public void logout() throws ApplicationException {
        try {
            databaseSession.useLoginRole();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to restore database login session", e);
        }
    }

    private UserRole parseRole(String role) throws ApplicationException {
        if (role == null || role.isBlank()) throw new ApplicationException("Authenticated user role is missing");
        try {
            return UserRole.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApplicationException("Unsupported authenticated user role: " + role, e);
        }
    }

    private String resolveAuthenticationError(LoginResultBean loginResult) {
        if (loginResult.getErrorMessage() != null && !loginResult.getErrorMessage().isBlank()) return loginResult.getErrorMessage();
        return "Authentication failed";
    }
}
