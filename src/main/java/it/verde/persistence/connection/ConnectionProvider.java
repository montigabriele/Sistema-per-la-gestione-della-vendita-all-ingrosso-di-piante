package it.verde.persistence.connection;

import it.verde.exception.DaoException;

import java.sql.Connection;

public interface ConnectionProvider {
    Connection getConnection() throws DaoException;
}
