package it.verde.controller;

import it.verde.exception.ApplicationException;
import it.verde.exception.DaoException;
import it.verde.mapper.UserAccountMapper;
import it.verde.model.entity.UserAccount;
import it.verde.model.type.UserRole;
import it.verde.persistence.dao.UserDao;
import it.verde.view.bean.UserBean;

import java.util.List;
import java.util.Objects;

public final class UserController {
    private final UserDao userDao;
    private final UserAccountMapper userAccountMapper;

    public UserController(UserDao userDao, UserAccountMapper userAccountMapper) {
        this.userDao = Objects.requireNonNull(userDao);
        this.userAccountMapper = Objects.requireNonNull(userAccountMapper);
    }

    public boolean usernameExists(String username) throws ApplicationException {
        String normalizedUsername = requireUsername(username);
        try {
            return userDao.existsByUsername(normalizedUsername);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify username", e);
        }
    }

    public boolean createUser(UserBean bean) throws ApplicationException {
        UserAccount user = mapAndValidateForCreation(bean);
        if (user.getRole() == UserRole.ADMINISTRATOR) throw new ApplicationException("Creating additional administrator accounts is not allowed");
        if (usernameExists(user.getUsername())) return false;

        try {
            return userDao.create(user, bean.getPassword());
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public boolean deleteUser(String username) throws ApplicationException {
        String normalizedUsername = requireUsername(username);
        try {
            return userDao.deleteByUsername(normalizedUsername);
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }
    
    public List<UserBean> getAllUsers() throws ApplicationException {
        try {
            return userDao.findAll().stream().map(userAccountMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve users", e);
        }
    }

    private UserAccount mapAndValidateForCreation(UserBean bean) throws ApplicationException {
        if (bean == null) throw new ApplicationException("User data is required");
        requireUsername(bean.getUsername());
        if (bean.getPassword() == null || bean.getPassword().isBlank()) throw new ApplicationException("Password is required");
        if (bean.getRole() == null || bean.getRole().isBlank()) throw new ApplicationException("User role is required");

        try {
            return userAccountMapper.toEntity(bean);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ApplicationException("Unable to access user data", e);
        }
    }

    private String requireUsername(String username) throws ApplicationException {
        if (username == null || username.trim().isEmpty()) throw new ApplicationException("Username is required");
        return username.trim();
    }
}
