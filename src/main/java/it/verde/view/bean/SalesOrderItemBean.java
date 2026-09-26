package it.verde.view.bean;

import java.math.BigDecimal;

public class SalesOrderItemBean {
    private String speciesCode;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;

    public SalesOrderItemBean() {
        // empty
    }

    public String getSpeciesCode() { return speciesCode; }
    public void setSpeciesCode(String speciesCode) { this.speciesCode = validateSpeciesCode(speciesCode); }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getLineTotal() { return lineTotal; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }

    private String validateSpeciesCode(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim().toUpperCase();
        if (!normalized.matches("^SP-[FN]\\d{3}-(ES|IN)$")) throw new IllegalArgumentException("Invalid species code format");
        return normalized;
    }
}
