package it.verde.mapper;

import it.verde.model.entity.FloweringPlantSpecies;
import it.verde.model.entity.NonFloweringPlantSpecies;
import it.verde.model.entity.PlantSpecies;
import it.verde.model.type.FlowerColor;
import it.verde.model.type.PlantType;
import it.verde.view.bean.PlantSpeciesBean;

import java.util.Objects;

public final class PlantSpeciesMapper {
    public PlantSpecies toEntity(PlantSpeciesBean bean) {
        Objects.requireNonNull(bean, "Plant species bean cannot be null");
        PlantType plantType = PlantType.valueOf(Objects.requireNonNull(bean.getPlantType(), "Plant type cannot be null"));
        return toEntity(bean, plantType);
    }

    public PlantSpecies toEntity(PlantSpeciesBean bean, PlantType plantType) {
        Objects.requireNonNull(bean, "Plant species bean cannot be null");
        Objects.requireNonNull(plantType, "Plant type cannot be null");
        boolean exotic = Objects.requireNonNull(bean.getExotic(), "Exotic flag cannot be null");
        boolean flowering = Objects.requireNonNull(bean.getFlowering(), "Flowering flag cannot be null");
        if (flowering) {
            FlowerColor flowerColor = FlowerColor.valueOf(Objects.requireNonNull(bean.getFlowerColor(), "Flower color cannot be null for a flowering species"));
            return new FloweringPlantSpecies(bean.getSpeciesCode(), bean.getLatinName(), bean.getCommonName(), plantType, exotic, flowerColor);
        }
        return new NonFloweringPlantSpecies(bean.getSpeciesCode(), bean.getLatinName(), bean.getCommonName(), plantType, exotic);
    }

    public PlantSpeciesBean toBean(PlantSpecies entity) {
        Objects.requireNonNull(entity, "Plant species entity cannot be null");
        PlantSpeciesBean bean = new PlantSpeciesBean();
        bean.setSpeciesCode(entity.getSpeciesCode());
        bean.setLatinName(entity.getLatinName());
        bean.setCommonName(entity.getCommonName());
        bean.setPlantType(entity.getPlantType().name());
        bean.setExotic(entity.isExotic());
        bean.setFlowering(entity.isFlowering());
        if (entity instanceof FloweringPlantSpecies floweringSpecies) bean.setFlowerColor(floweringSpecies.getFlowerColor().name());
        return bean;
    }
}
