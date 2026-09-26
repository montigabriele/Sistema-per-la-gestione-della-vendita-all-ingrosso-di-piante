package it.verde.mapper;

import it.verde.model.entity.Address;
import it.verde.model.type.AddressType;
import it.verde.view.bean.AddressBean;

import java.util.Objects;

public final class AddressMapper {
    public Address toEntity(AddressBean bean) {
        Objects.requireNonNull(bean, "Address bean cannot be null");
        AddressType type = AddressType.valueOf(Objects.requireNonNull(bean.getType(), "Address type cannot be null"));
        return new Address(bean.getId(), type, bean.getStreet(), bean.getPostalCode(), bean.getCity());
    }

    public AddressBean toBean(Address entity) {
        Objects.requireNonNull(entity, "Address entity cannot be null");
        AddressBean bean = new AddressBean();
        bean.setId(entity.getId());
        bean.setType(entity.getType().name());
        bean.setStreet(entity.getStreet());
        bean.setPostalCode(entity.getPostalCode());
        bean.setCity(entity.getCity());
        return bean;
    }
}
