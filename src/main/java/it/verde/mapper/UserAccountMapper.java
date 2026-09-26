package it.verde.mapper;

import it.verde.model.entity.UserAccount;
import it.verde.model.type.UserRole;
import it.verde.view.bean.UserBean;

import java.util.Objects;

public final class UserAccountMapper {
    public UserAccount toEntity(UserBean bean) {
        Objects.requireNonNull(bean, "User bean cannot be null");
        UserRole role = UserRole.valueOf(Objects.requireNonNull(bean.getRole(), "User role cannot be null"));
        return new UserAccount(bean.getUsername(), role);
    }

    public UserBean toBean(UserAccount entity) {
        Objects.requireNonNull(entity, "User account entity cannot be null");
        UserBean bean = new UserBean();
        bean.setUsername(entity.getUsername());
        bean.setRole(entity.getRole().name());
        return bean;
    }
}
