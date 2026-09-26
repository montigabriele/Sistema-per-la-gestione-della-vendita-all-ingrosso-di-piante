package it.verde.mapper;

import it.verde.model.entity.WarehouseStock;
import it.verde.view.bean.WarehouseStockBean;

import java.util.Objects;

public final class WarehouseStockMapper {
    public WarehouseStock toEntity(WarehouseStockBean bean) {
        Objects.requireNonNull(bean, "Warehouse stock bean cannot be null");
        int quantity = Objects.requireNonNull(bean.getQuantity(), "Quantity cannot be null");
        return new WarehouseStock(bean.getSpeciesCode(), quantity);
    }

    public WarehouseStockBean toBean(WarehouseStock entity) {
        Objects.requireNonNull(entity, "Warehouse stock entity cannot be null");
        WarehouseStockBean bean = new WarehouseStockBean();
        bean.setSpeciesCode(entity.getSpeciesCode());
        bean.setQuantity(entity.getQuantity());
        return bean;
    }
}
