package it.verde.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RetailCompany {
    private String vatNumber;
    private String companyName;
    private String contactFirstName;
    private String contactLastName;
    private Address legalAddress;
    private Address billingAddress;
    private List<Contact> contacts;

    public RetailCompany(String vatNumber, String companyName, String contactFirstName, String contactLastName, Address legalAddress, Address billingAddress) {
        this.vatNumber = Objects.requireNonNull(vatNumber);
        this.companyName = Objects.requireNonNull(companyName);
        this.contactFirstName = Objects.requireNonNull(contactFirstName);
        this.contactLastName = Objects.requireNonNull(contactLastName);
        this.legalAddress = Objects.requireNonNull(legalAddress);
        this.billingAddress = billingAddress != null ? billingAddress : legalAddress;
        this.contacts = new ArrayList<>();
    }

    public String getVatNumber() { return vatNumber; }
    public void setVatNumber(String vatNumber) { this.vatNumber = vatNumber; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getContactFirstName() { return contactFirstName; }
    public void setContactFirstName(String contactFirstName) { this.contactFirstName = contactFirstName; }
    public String getContactLastName() { return contactLastName; }
    public void setContactLastName(String contactLastName) { this.contactLastName = contactLastName; }
    public Address getLegalAddress() { return legalAddress; }
    public void setLegalAddress(Address legalAddress) { this.legalAddress = legalAddress; }
    public Address getBillingAddress() { return billingAddress; }
    public void setBillingAddress(Address billingAddress) { this.billingAddress = billingAddress != null ? billingAddress : legalAddress; }
    public List<Contact> getContacts() { return List.copyOf(contacts); }
    public void setContacts(List<Contact> contacts) { this.contacts = contacts == null ? new ArrayList<>() : new ArrayList<>(contacts); }

    public void addContact(Contact contact) {
        if (contact != null) contacts.add(contact);
    }

    public String getContactFullName() {
        return contactFirstName + " " + contactLastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RetailCompany company)) return false;
        return vatNumber.equals(company.vatNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vatNumber);
    }
}
