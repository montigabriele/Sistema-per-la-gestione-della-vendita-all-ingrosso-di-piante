package it.verde.view.bean;

import java.util.ArrayList;
import java.util.List;

public class RetailCompanyBean {
    private String vatNumber;
    private String companyName;
    private String contactFirstName;
    private String contactLastName;
    private AddressBean legalAddress;
    private AddressBean billingAddress;
    private List<ContactBean> contacts = new ArrayList<>();

    public RetailCompanyBean() {
        // empty
    }

    public String getVatNumber() { return vatNumber; }
    public void setVatNumber(String vatNumber) { this.vatNumber = validateVatNumber(vatNumber); }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getContactFirstName() { return contactFirstName; }
    public void setContactFirstName(String contactFirstName) { this.contactFirstName = contactFirstName; }
    public String getContactLastName() { return contactLastName; }
    public void setContactLastName(String contactLastName) { this.contactLastName = contactLastName; }
    public AddressBean getLegalAddress() { return legalAddress; }
    public void setLegalAddress(AddressBean legalAddress) { this.legalAddress = legalAddress; }
    public AddressBean getBillingAddress() { return billingAddress; }
    public void setBillingAddress(AddressBean billingAddress) { this.billingAddress = billingAddress; }
    public List<ContactBean> getContacts() { return contacts; }
    public void setContacts(List<ContactBean> contacts) { this.contacts = contacts != null ? contacts : new ArrayList<>(); }

    private String validateVatNumber(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim();
        if (!normalized.matches("^\\d{11}$")) throw new IllegalArgumentException("VAT number must contain 11 digits");
        return normalized;
    }
}
