package it.verde.model.entity;

import it.verde.model.type.PlantType;

import java.util.Objects;

public abstract class PlantSpecies {
    private String speciesCode;
    private final String latinName;
    private final String commonName;
    private PlantType plantType;
    private final boolean exotic;

    protected PlantSpecies(String speciesCode, String latinName, String commonName, PlantType plantType, boolean exotic) {
        this.speciesCode = speciesCode;
        this.latinName = latinName;
        this.commonName = commonName;
        this.plantType = Objects.requireNonNull(plantType, "Plant type cannot be null");
        this.exotic = exotic;
    }

    public String getSpeciesCode() { return speciesCode; }
    public void setSpeciesCode(String speciesCode) { this.speciesCode = speciesCode; }
    public String getLatinName() { return latinName; }
    public String getCommonName() { return commonName; }
    public PlantType getPlantType() { return plantType; }
    public void setPlantType(PlantType plantType) { this.plantType = Objects.requireNonNull(plantType, "Plant type cannot be null"); }
    public boolean isExotic() { return exotic; }
    public abstract boolean isFlowering();
}
