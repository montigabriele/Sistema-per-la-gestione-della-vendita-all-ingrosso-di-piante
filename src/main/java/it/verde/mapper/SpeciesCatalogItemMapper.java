package it.verde.mapper;

import it.verde.model.readmodel.SpeciesCatalogEntry;
import it.verde.view.bean.SpeciesCatalogItemBean;

import java.util.Objects;

public final class SpeciesCatalogItemMapper {
    public SpeciesCatalogItemBean toBean(SpeciesCatalogEntry model) {
        Objects.requireNonNull(model, "Species catalog entry cannot be null");
        SpeciesCatalogItemBean bean = new SpeciesCatalogItemBean();
        bean.setSpeciesCode(model.speciesCode());
        bean.setCommonName(model.commonName());
        bean.setLatinName(model.latinName());
        bean.setPlantType(model.plantType() == null ? null : model.plantType().name());
        bean.setExotic(model.exotic());
        bean.setFlowerColor(model.flowerColor() == null ? null : model.flowerColor().name());
        bean.setCurrentPrice(model.currentPrice());
        bean.setStockQuantity(model.stockQuantity());
        return bean;
    }
}
