package it.verde.model.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class SalesOrderItem {
    private String speciesCode;
    private int quantity;
    private final BigDecimal unitPrice;

    public SalesOrderItem(String speciesCode, int quantity, BigDecimal unitPrice) {
        setSpeciesCode(speciesCode);
        setQuantity(quantity);
        this.unitPrice = Objects.requireNonNull(unitPrice, "Unit price cannot be null");
        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Unit price must be greater than zero");
    }

    public BigDecimal calculateTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
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
    public BigDecimal getUnitPrice() { return unitPrice; }
}
