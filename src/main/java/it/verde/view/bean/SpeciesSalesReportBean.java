package it.verde.view.bean;

import java.math.BigDecimal;

public class SpeciesSalesReportBean {
    private String speciesCode;
    private String commonName;
    private String plantType;
    private Boolean flowering;
    private Integer soldQuantity;
    private BigDecimal totalSalesValue;

    public SpeciesSalesReportBean() {
        // empty
    }

    public String getSpeciesCode() { return speciesCode; }
    public void setSpeciesCode(String speciesCode) { this.speciesCode = speciesCode; }
    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }
    public String getPlantType() { return plantType; }
    public void setPlantType(String plantType) { this.plantType = plantType; }
    public Boolean getFlowering() { return flowering; }
    public void setFlowering(Boolean flowering) { this.flowering = flowering; }
    public Integer getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(Integer soldQuantity) { this.soldQuantity = soldQuantity; }
    public BigDecimal getTotalSalesValue() { return totalSalesValue; }
    public void setTotalSalesValue(BigDecimal totalSalesValue) { this.totalSalesValue = totalSalesValue; }
}
