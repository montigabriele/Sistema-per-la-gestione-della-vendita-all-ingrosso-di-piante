package it.verde.model.entity;

import it.verde.model.type.PlantType;

public class NonFloweringPlantSpecies extends PlantSpecies {
    public NonFloweringPlantSpecies(String speciesCode, String latinName, String commonName, PlantType plantType, boolean exotic) {
        super(speciesCode, latinName, commonName, plantType, exotic);
    }

    @Override
    public boolean isFlowering() { return false; }
}
