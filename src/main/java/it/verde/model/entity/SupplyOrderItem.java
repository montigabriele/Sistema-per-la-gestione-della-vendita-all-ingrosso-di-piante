package it.verde.model.entity;

import java.util.Objects;

public class SupplyOrderItem {
    private String speciesCode;
    private int quantity;

    public SupplyOrderItem(String speciesCode, int quantity) {
        setSpeciesCode(speciesCode);
        setQuantity(quantity);
    }

    public String getSpeciesCode() { return speciesCode; }
    public void setSpeciesCode(String speciesCode) {
        if (speciesCode == null || speciesCode.isBlank()) throw new IllegalArgumentException("Species code is required");
        this.speciesCode = speciesCode.trim().toUpperCase();
    }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupplyOrderItem item)) return false;
        return Objects.equals(speciesCode, item.speciesCode);
    }

    @Override
    public int hashCode() { return Objects.hash(speciesCode); }
}
