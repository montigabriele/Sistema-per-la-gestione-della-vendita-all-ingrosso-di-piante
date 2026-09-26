package it.verde.view.bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceHistoryBean {
    private LocalDateTime changedAt;
    private BigDecimal previousPrice;
    private BigDecimal currentPrice;
    private String speciesCode;

    public PriceHistoryBean() {
        // empty
    }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
    public BigDecimal getPreviousPrice() { return previousPrice; }
    public void setPreviousPrice(BigDecimal previousPrice) { this.previousPrice = previousPrice; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public String getSpeciesCode() { return speciesCode; }
    public void setSpeciesCode(String speciesCode) { this.speciesCode = validateSpeciesCode(speciesCode); }

    private String validateSpeciesCode(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim().toUpperCase();
        if (!normalized.matches("^SP-[FN]\\d{3}-(ES|IN)$")) throw new IllegalArgumentException("Invalid species code format");
        return normalized;
    }
}
