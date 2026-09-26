package it.verde.view.bean;

public class AddressBean {
    private Integer id;
    private String type;
    private String street;
    private String postalCode;
    private String city;

    public AddressBean() {
        // empty
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = validateType(type); }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = validatePostalCode(postalCode); }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    private String validateType(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim().toUpperCase();
        if (!normalized.equals("LEGAL") && !normalized.equals("BILLING")) throw new IllegalArgumentException("Address type must be LEGAL or BILLING");
        return normalized;
    }

    private String validatePostalCode(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim();
        if (!normalized.matches("^\\d{5}$")) throw new IllegalArgumentException("Postal code must contain 5 digits");
        return normalized;
    }
}
