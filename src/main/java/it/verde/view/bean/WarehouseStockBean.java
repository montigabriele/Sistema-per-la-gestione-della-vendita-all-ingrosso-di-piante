package it.verde.view.bean;

public class WarehouseStockBean {
    private String speciesCode;
    private Integer quantity;

    public String getSpeciesCode() { return speciesCode; }
    public void setSpeciesCode(String speciesCode) { this.speciesCode = validateSpeciesCode(speciesCode); }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    private String validateSpeciesCode(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim().toUpperCase();
        if (!normalized.matches("^SP-[FN]\\d{3}-(ES|IN)$")) throw new IllegalArgumentException("Invalid species code format");
        return normalized;
    }
}
