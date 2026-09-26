package it.verde.mapper;

import it.verde.model.entity.SupplyOrderItem;
import it.verde.view.bean.SupplyOrderItemBean;

import java.util.Objects;

public final class SupplyOrderItemMapper {
    public SupplyOrderItem toEntity(SupplyOrderItemBean bean) {
        Objects.requireNonNull(bean, "Supply order item bean cannot be null");
        int quantity = Objects.requireNonNull(bean.getQuantity(), "Quantity cannot be null");
        return new SupplyOrderItem(bean.getSpeciesCode(), quantity);
    }

    public SupplyOrderItemBean toBean(SupplyOrderItem entity) {
        Objects.requireNonNull(entity, "Supply order item entity cannot be null");
        SupplyOrderItemBean bean = new SupplyOrderItemBean();
        bean.setSpeciesCode(entity.getSpeciesCode());
        bean.setQuantity(entity.getQuantity());
        return bean;
    }
}
