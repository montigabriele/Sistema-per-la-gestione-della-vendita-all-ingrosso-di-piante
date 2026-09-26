package it.verde.model.readmodel;

import it.verde.model.type.PlantType;

import java.math.BigDecimal;

public record SpeciesSalesReport(String speciesCode, String commonName, PlantType plantType, boolean flowering, int soldQuantity, BigDecimal totalSalesValue) {}
