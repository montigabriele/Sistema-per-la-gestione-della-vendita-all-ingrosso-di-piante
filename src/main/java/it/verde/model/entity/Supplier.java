package it.verde.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Supplier {
    private Integer supplierId;
    private String name;
    private String taxCode;
    private List<Address> addresses;

    public Supplier() {
        this.addresses = new ArrayList<>();
    }

    public Supplier(String name, String taxCode) {
        this();
        this.name = name;
        this.taxCode = taxCode;
    }

    public Supplier(Integer supplierId, String name, String taxCode) {
        this();
        this.supplierId = supplierId;
        this.name = name;
        this.taxCode = taxCode;
    }

    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTaxCode() { return taxCode; }
    public void setTaxCode(String taxCode) { this.taxCode = taxCode; }
    public List<Address> getAddresses() { return List.copyOf(addresses); }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses == null ? new ArrayList<>() : new ArrayList<>(addresses); }

    public void addAddress(Address address) {
        addresses.add(Objects.requireNonNull(address, "Address cannot be null"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Supplier supplier)) return false;
        if (supplierId == null || supplier.supplierId == null) return false;
        return supplierId.equals(supplier.supplierId);
    }

    @Override
    public int hashCode() {
        return supplierId == null ? 0 : supplierId.hashCode();
    }
}
