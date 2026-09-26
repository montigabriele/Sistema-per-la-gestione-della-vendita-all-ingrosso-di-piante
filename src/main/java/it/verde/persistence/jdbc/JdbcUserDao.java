package it.verde.persistence.jdbc;

import it.verde.exception.DaoException;
import it.verde.model.entity.UserAccount;
import it.verde.model.type.UserRole;
import it.verde.persistence.connection.ConnectionProvider;
import it.verde.persistence.dao.UserDao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class JdbcUserDao extends AbstractJdbcDao implements UserDao {
    private static final String INSERT_USER_PROCEDURE = "{CALL InserisciUtente(?, ?, ?)}";
    private static final String DELETE_USER_PROCEDURE = "{CALL EliminaUtente(?)}";
    private static final String FIND_ALL_USERS_PROCEDURE = "{CALL VisualizzaUtenti()}";
    private static final String EXISTS_BY_USERNAME_PROCEDURE = "{CALL VerificaUsernameEsistente(?)}";

    public JdbcUserDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public boolean existsByUsername(String username) throws DaoException {
        if (username == null || username.isBlank()) return false;

        try (CallableStatement statement = connection().prepareCall(EXISTS_BY_USERNAME_PROCEDURE)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean("Esiste");
            }
        } catch (SQLException e) {
            throw dataAccessException("Unable to verify username", e);
        }
    }

    @Override
    public boolean create(UserAccount user, String password) throws DaoException {
        if (user == null) throw new DaoException("User is required");
        if (user.getUsername() == null || user.getUsername().isBlank()) throw new DaoException("Username is required");
        if (password == null || password.isEmpty()) throw new DaoException("Password is required");
        if (user.getRole() == null) throw new DaoException("User role is required");

        try (CallableStatement statement = connection().prepareCall(INSERT_USER_PROCEDURE)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, password);
            statement.setString(3, toDbRole(user.getRole()));
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw procedureException("Unable to create user", e);
        }
    }

    @Override
    public boolean deleteByUsername(String username) throws DaoException {
        if (username == null || username.isBlank()) throw new DaoException("Username is required");

        try (CallableStatement statement = connection().prepareCall(DELETE_USER_PROCEDURE)) {
            statement.setString(1, username);
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw procedureException("Unable to delete user", e);
        }
    }

    @Override
    public List<UserAccount> findAll() throws DaoException {
        List<UserAccount> users = new ArrayList<>();

        try (CallableStatement statement = connection().prepareCall(FIND_ALL_USERS_PROCEDURE); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) users.add(new UserAccount(resultSet.getString("Username"), fromDbRole(resultSet.getString("Ruolo"))));
            return users;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve users", e);
        }
    }

    private String toDbRole(UserRole role) {
        return switch (role) {
            case ADMINISTRATOR -> "amministratore";
            case SALES_MANAGER -> "responsabileCommerciale";
            case LOGISTICS_MANAGER -> "responsabileLogistico";
        };
    }

    private UserRole fromDbRole(String value) throws DaoException {
        if (value == null) throw new DaoException("Database returned a null user role");
        return switch (value) {
            case "amministratore" -> UserRole.ADMINISTRATOR;
            case "responsabileCommerciale" -> UserRole.SALES_MANAGER;
            case "responsabileLogistico" -> UserRole.LOGISTICS_MANAGER;
            default -> throw new DaoException("Unsupported database user role: " + value);
        };
    }
}
