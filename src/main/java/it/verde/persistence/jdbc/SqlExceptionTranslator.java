package it.verde.persistence.jdbc;

import it.verde.exception.DaoException;

import java.sql.SQLException;

final class SqlExceptionTranslator {
    private static final String BUSINESS_SQL_STATE = "45000";

    private SqlExceptionTranslator() {
    }

    static DaoException translate(String fallbackMessage, SQLException cause) {
        if (BUSINESS_SQL_STATE.equals(cause.getSQLState())) {
            String databaseMessage = cause.getMessage();
            if (databaseMessage != null && !databaseMessage.isBlank()) {
                return new DaoException(databaseMessage.trim(), cause);
            }
        }
        return new DaoException(fallbackMessage, cause);
    }
}