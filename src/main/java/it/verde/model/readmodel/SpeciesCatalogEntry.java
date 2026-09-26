package it.verde.model.readmodel;

import it.verde.model.type.FlowerColor;
import it.verde.model.type.PlantType;

import java.math.BigDecimal;

public record SpeciesCatalogEntry(String speciesCode, String commonName, String latinName, PlantType plantType, boolean exotic, FlowerColor flowerColor, BigDecimal currentPrice, int stockQuantity) {}
