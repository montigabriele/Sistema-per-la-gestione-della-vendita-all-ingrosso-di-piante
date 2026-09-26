package it.verde.view.bean;

import java.math.BigDecimal;

public class SpeciesCatalogItemBean {
    private String speciesCode;
    private String commonName;
    private String latinName;
    private String plantType;
    private Boolean exotic;
    private String flowerColor;
    private BigDecimal currentPrice;
    private Integer stockQuantity;

    public String getSpeciesCode() { return speciesCode; }
    public void setSpeciesCode(String speciesCode) { this.speciesCode = speciesCode; }
    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }
    public String getLatinName() { return latinName; }
    public void setLatinName(String latinName) { this.latinName = latinName; }
    public String getPlantType() { return plantType; }
    public void setPlantType(String plantType) { this.plantType = plantType; }
    public Boolean getExotic() { return exotic; }
    public void setExotic(Boolean exotic) { this.exotic = exotic; }
    public String getFlowerColor() { return flowerColor; }
    public void setFlowerColor(String flowerColor) { this.flowerColor = flowerColor; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
}
