package it.verde.mapper;

import it.verde.model.readmodel.AuthenticationResult;
import it.verde.view.bean.LoginResultBean;

import java.util.Objects;

public final class AuthenticationResultMapper {
    public LoginResultBean toBean(AuthenticationResult result) {
        Objects.requireNonNull(result, "Authentication result cannot be null");
        LoginResultBean bean = new LoginResultBean();
        bean.setUsername(result.username());
        bean.setRole(result.role() == null ? null : result.role().name());
        bean.setErrorCode(result.errorCode());
        bean.setErrorMessage(result.errorMessage());
        return bean;
    }
}
