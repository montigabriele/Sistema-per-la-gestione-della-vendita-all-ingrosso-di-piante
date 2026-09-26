package it.verde.model.entity;

import it.verde.model.type.ContactType;

import java.util.Objects;

public record Contact(ContactType type, String value) {
    public Contact {
        Objects.requireNonNull(type, "Contact type cannot be null");
        Objects.requireNonNull(value, "Contact value cannot be null");
    }
}
