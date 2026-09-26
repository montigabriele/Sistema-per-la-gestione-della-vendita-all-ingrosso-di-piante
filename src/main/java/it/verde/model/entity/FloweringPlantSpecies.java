package it.verde.model.entity;

import it.verde.model.type.FlowerColor;
import it.verde.model.type.PlantType;

import java.util.Objects;

public class FloweringPlantSpecies extends PlantSpecies {
    private final FlowerColor flowerColor;

    public FloweringPlantSpecies(String speciesCode, String latinName, String commonName, PlantType plantType, boolean exotic, FlowerColor flowerColor) {
        super(speciesCode, latinName, commonName, plantType, exotic);
        this.flowerColor = Objects.requireNonNull(flowerColor, "Flower color cannot be null for a flowering species");
    }

    public FlowerColor getFlowerColor() { return flowerColor; }

    @Override
    public boolean isFlowering() { return true; }
}
