package it.verde.mapper;

import it.verde.model.entity.Contact;
import it.verde.model.type.ContactType;
import it.verde.view.bean.ContactBean;

import java.util.Objects;

public final class ContactMapper {
    public Contact toEntity(ContactBean bean) {
        Objects.requireNonNull(bean, "Contact bean cannot be null");
        ContactType type = ContactType.valueOf(Objects.requireNonNull(bean.getType(), "Contact type cannot be null"));
        return new Contact(type, bean.getValue());
    }

    public ContactBean toBean(Contact entity) {
        Objects.requireNonNull(entity, "Contact entity cannot be null");
        ContactBean bean = new ContactBean();
        bean.setType(entity.type().name());
        bean.setValue(entity.value());
        return bean;
    }
}
