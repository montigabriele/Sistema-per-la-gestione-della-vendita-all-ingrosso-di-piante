package it.verde.model.entity;

import it.verde.model.type.AddressType;

import java.util.Objects;

public class Address {
    private Integer id;
    private AddressType type;
    private final String street;
    private final String postalCode;
    private final String city;

    public Address(Integer id, AddressType type, String street, String postalCode, String city) {
        this.id = id;
        this.type = Objects.requireNonNull(type, "Address type cannot be null");
        this.street = Objects.requireNonNull(street, "Street cannot be null");
        this.postalCode = Objects.requireNonNull(postalCode, "Postal code cannot be null");
        this.city = Objects.requireNonNull(city, "City cannot be null");
    }

    public Address(AddressType type, String street, String postalCode, String city) {
        this(null, type, street, postalCode, city);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public AddressType getType() { return type; }
    public void setType(AddressType type) { this.type = type; }
    public String getStreet() { return street; }
    public String getPostalCode() { return postalCode; }
    public String getCity() { return city; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        if (id != null && address.id != null) return Objects.equals(id, address.id);
        return type == address.type && Objects.equals(street, address.street) && Objects.equals(postalCode, address.postalCode) && Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : Objects.hash(type, street, postalCode, city);
    }
}
