package it.verde.persistence.jdbc;

import it.verde.exception.DaoException;
import it.verde.persistence.connection.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public abstract class AbstractJdbcDao {
    private final ConnectionProvider connectionProvider;

    protected AbstractJdbcDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = Objects.requireNonNull(connectionProvider);
    }

    protected final Connection connection() throws DaoException {
        return connectionProvider.getConnection();
    }

    protected final DaoException dataAccessException(String message, SQLException cause) {
        return new DaoException(message, cause);
    }

    protected final DaoException procedureException(String fallbackMessage, SQLException cause) {
        return SqlExceptionTranslator.translate(fallbackMessage, cause);
    }
}