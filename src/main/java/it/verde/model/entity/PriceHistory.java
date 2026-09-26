package it.verde.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceHistory {
    private final LocalDateTime changedAt;
    private final BigDecimal previousPrice;
    private final BigDecimal currentPrice;
    private String speciesCode;

    public PriceHistory(LocalDateTime changedAt, BigDecimal previousPrice, BigDecimal currentPrice, String speciesCode) {
        this.changedAt = changedAt;
        this.previousPrice = previousPrice;
        this.currentPrice = currentPrice;
        this.speciesCode = speciesCode;
    }

    public LocalDateTime getChangedAt() { return changedAt; }
    public BigDecimal getPreviousPrice() { return previousPrice; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public String getSpeciesCode() { return speciesCode; }
    public void setSpeciesCode(String speciesCode) { this.speciesCode = speciesCode; }
}
