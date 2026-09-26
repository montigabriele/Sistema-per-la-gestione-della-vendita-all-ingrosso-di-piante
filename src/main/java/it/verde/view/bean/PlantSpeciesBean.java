package it.verde.view.bean;

public class PlantSpeciesBean {
    private String speciesCode;
    private String latinName;
    private String commonName;
    private String plantType;
    private Boolean exotic;
    private Boolean flowering;
    private String flowerColor;

    public PlantSpeciesBean() {
        // empty
    }

    public String getSpeciesCode() { return speciesCode; }
    public void setSpeciesCode(String speciesCode) { this.speciesCode = validateSpeciesCode(speciesCode); }
    public String getLatinName() { return latinName; }
    public void setLatinName(String latinName) { this.latinName = latinName; }
    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }
    public String getPlantType() { return plantType; }
    public void setPlantType(String plantType) { this.plantType = validatePlantType(plantType); }
    public Boolean getExotic() { return exotic; }
    public void setExotic(Boolean exotic) { this.exotic = exotic; }
    public Boolean getFlowering() { return flowering; }
    public void setFlowering(Boolean flowering) { this.flowering = flowering; }
    public String getFlowerColor() { return flowerColor; }
    public void setFlowerColor(String flowerColor) { this.flowerColor = validateFlowerColor(flowerColor); }

    private String validateSpeciesCode(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim().toUpperCase();
        if (!normalized.matches("^SP-[FN]\\d{3}-(ES|IN)$")) throw new IllegalArgumentException("Invalid species code format");
        return normalized;
    }

    private String validatePlantType(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim().toUpperCase();
        if (!normalized.equals("INDOOR") && !normalized.equals("GARDEN")) throw new IllegalArgumentException("Plant type must be INDOOR or GARDEN");
        return normalized;
    }

    private String validateFlowerColor(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim().toUpperCase();
        if (!normalized.matches("RED|YELLOW|ORANGE|BLUE|PURPLE|WHITE|MULTICOLOR")) throw new IllegalArgumentException("Invalid flower color");
        return normalized;
    }
}
