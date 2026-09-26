package it.verde.controller.factory;

import it.verde.controller.navigation.RoleController;
import it.verde.exception.ApplicationException;
import it.verde.model.type.UserRole;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class RoleControllerRouter {
    private final Map<UserRole, RoleControllerFactory> factories = new EnumMap<>(UserRole.class);

    public RoleControllerRouter(List<RoleControllerFactory> factories) {
        Objects.requireNonNull(factories, "Role controller factories cannot be null");
        for (RoleControllerFactory factory : factories) register(factory);
    }

    private void register(RoleControllerFactory factory) {
        RoleControllerFactory nonNullFactory = Objects.requireNonNull(factory, "Role controller factory cannot be null");
        UserRole role = Objects.requireNonNull(nonNullFactory.supportedRole(), "Supported role cannot be null");
        if (factories.putIfAbsent(role, nonNullFactory) != null) throw new IllegalArgumentException("Duplicate role controller factory for role: " + role);
    }

    public RoleController resolve(UserRole role) throws ApplicationException {
        if (role == null) throw new ApplicationException("User role is required");
        RoleControllerFactory factory = factories.get(role);
        if (factory == null) throw new ApplicationException("Unsupported user role: " + role);
        return factory.create();
    }
}
