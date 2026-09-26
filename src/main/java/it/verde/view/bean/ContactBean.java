package it.verde.view.bean;

public class ContactBean {
    private String type;
    private String value;

    public ContactBean() {
        // empty
    }

    public String getType() { return type; }
    public void setType(String type) {
        this.type = validateType(type);
        validateValue(this.value, this.type);
    }

    public String getValue() { return value; }
    public void setValue(String value) {
        validateValue(value, this.type);
        this.value = value;
    }

    private String validateType(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim().toUpperCase();
        if (!normalized.equals("PHONE") && !normalized.equals("MOBILE") && !normalized.equals("EMAIL")) throw new IllegalArgumentException("Contact type must be PHONE, MOBILE or EMAIL");
        return normalized;
    }

    private void validateValue(String value, String contactType) {
        if (value == null || value.isBlank() || contactType == null || contactType.isBlank()) return;
        if (contactType.equals("EMAIL") && !value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) throw new IllegalArgumentException("Invalid email format");
        if ((contactType.equals("PHONE") || contactType.equals("MOBILE")) && !value.matches("^[+]?\\d{8,15}$")) throw new IllegalArgumentException("Invalid phone format");
    }
}
