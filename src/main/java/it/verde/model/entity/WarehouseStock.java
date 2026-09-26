package it.verde.model.entity;

public class WarehouseStock {
    private final String speciesCode;
    private int quantity;

    public WarehouseStock(String speciesCode, int quantity) {
        if (speciesCode == null || speciesCode.isBlank()) throw new IllegalArgumentException("Species code is required");
        this.speciesCode = speciesCode.trim().toUpperCase();
        setQuantity(quantity);
    }

    public String getSpeciesCode() { return speciesCode; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Warehouse quantity cannot be negative");
        this.quantity = quantity;
    }
}
