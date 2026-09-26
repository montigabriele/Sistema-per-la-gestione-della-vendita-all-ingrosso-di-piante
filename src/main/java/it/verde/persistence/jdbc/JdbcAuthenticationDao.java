package it.verde.persistence.jdbc;

import it.verde.exception.DaoException;
import it.verde.model.readmodel.AuthenticationResult;
import it.verde.model.type.UserRole;
import it.verde.persistence.connection.ConnectionProvider;
import it.verde.persistence.dao.AuthenticationDao;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public final class JdbcAuthenticationDao extends AbstractJdbcDao implements AuthenticationDao {
    private static final String LOGIN_PROCEDURE = "{CALL login(?, ?, ?, ?, ?)}";

    public JdbcAuthenticationDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public AuthenticationResult authenticate(String username, String password) throws DaoException {
        if (username == null || username.isBlank()) throw new DaoException("Username is required");
        if (password == null || password.isEmpty()) throw new DaoException("Password is required");

        try (CallableStatement statement = connection().prepareCall(LOGIN_PROCEDURE)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.registerOutParameter(3, Types.INTEGER);
            statement.registerOutParameter(4, Types.INTEGER);
            statement.registerOutParameter(5, Types.VARCHAR);
            statement.execute();

            int roleCode = statement.getInt(3);
            int errorCode = statement.getInt(4);
            String errorMessage = statement.getString(5);
            UserRole role = mapRole(roleCode);

            return new AuthenticationResult(username, role, errorCode, errorMessage);
        } catch (SQLException e) {
            throw dataAccessException("Unable to execute login procedure", e);
        }
    }

    private UserRole mapRole(int roleCode) throws DaoException {
        return switch (roleCode) {
            case -1, 0 -> null;
            case 1 -> UserRole.ADMINISTRATOR;
            case 2 -> UserRole.SALES_MANAGER;
            case 3 -> UserRole.LOGISTICS_MANAGER;
            default -> throw new DaoException("Unsupported role code returned by login procedure: " + roleCode);
        };
    }
}
