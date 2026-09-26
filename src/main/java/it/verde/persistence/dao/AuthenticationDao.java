package it.verde.persistence.dao;

import it.verde.exception.DaoException;
import it.verde.model.readmodel.AuthenticationResult;

public interface AuthenticationDao {
    AuthenticationResult authenticate(String username, String password) throws DaoException;
}
