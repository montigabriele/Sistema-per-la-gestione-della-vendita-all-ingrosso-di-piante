package it.verde.persistence.dao;

import it.verde.exception.DaoException;
import it.verde.model.entity.UserAccount;

import java.util.List;

public interface UserDao {
    boolean existsByUsername(String username) throws DaoException;
    boolean create(UserAccount user, String password) throws DaoException;
    boolean deleteByUsername(String username) throws DaoException;
    List<UserAccount> findAll() throws DaoException;
}
