package it.verde.view.bean;

import java.util.ArrayList;
import java.util.List;

public class SupplierBean {
    private Integer supplierId;
    private String name;
    private String taxCode;
    private List<AddressBean> addresses = new ArrayList<>();

    public SupplierBean() {
        // empty
    }

    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTaxCode() { return taxCode; }
    public void setTaxCode(String taxCode) { this.taxCode = validateTaxCode(taxCode); }
    public List<AddressBean> getAddresses() { return addresses; }
    public void setAddresses(List<AddressBean> addresses) { this.addresses = addresses != null ? addresses : new ArrayList<>(); }

    private String validateTaxCode(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim().toUpperCase();
        boolean fiscalCode = normalized.matches("^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$");
        boolean vatNumber = normalized.matches("^\\d{11}$");
        if (!fiscalCode && !vatNumber) throw new IllegalArgumentException("Tax code must be a 16-character fiscal code or an 11-digit VAT number");
        return normalized;
    }
}
