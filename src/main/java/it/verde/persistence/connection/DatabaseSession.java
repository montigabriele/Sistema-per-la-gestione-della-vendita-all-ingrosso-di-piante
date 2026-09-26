package it.verde.persistence.connection;

import it.verde.exception.DaoException;
import it.verde.model.type.UserRole;

public interface DatabaseSession extends AutoCloseable {
    void useLoginRole() throws DaoException;
    void useRole(UserRole role) throws DaoException;
    boolean isValid(int timeoutSeconds) throws DaoException;

    @Override
    void close() throws DaoException;
}
