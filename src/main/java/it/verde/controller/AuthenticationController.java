package it.verde.controller;

import it.verde.exception.ApplicationException;
import it.verde.exception.DaoException;
import it.verde.mapper.AuthenticationResultMapper;
import it.verde.model.readmodel.AuthenticationResult;
import it.verde.persistence.dao.AuthenticationDao;
import it.verde.view.bean.LoginCredentialsBean;
import it.verde.view.bean.LoginResultBean;

import java.util.Objects;

public final class AuthenticationController {
    private final AuthenticationDao authenticationDao;
    private final AuthenticationResultMapper authenticationResultMapper;

    public AuthenticationController(AuthenticationDao authenticationDao, AuthenticationResultMapper authenticationResultMapper) {
        this.authenticationDao = Objects.requireNonNull(authenticationDao);
        this.authenticationResultMapper = Objects.requireNonNull(authenticationResultMapper);
    }

    public LoginResultBean authenticate(LoginCredentialsBean credentials) throws ApplicationException {
        if (credentials == null) throw new ApplicationException("Login credentials are required");
        String username = requireUsername(credentials.getUsername());
        String password = requirePassword(credentials.getPassword());

        try {
            AuthenticationResult result = authenticationDao.authenticate(username, password);
            return authenticationResultMapper.toBean(result);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to authenticate user", e);
        }
    }

    private String requireUsername(String username) throws ApplicationException {
        if (username == null || username.trim().isEmpty()) throw new ApplicationException("Username is required");
        return username.trim();
    }

    private String requirePassword(String password) throws ApplicationException {
        if (password == null || password.isEmpty()) throw new ApplicationException("Password is required");
        return password;
    }
}
