package it.verde.mapper;

import it.verde.model.entity.SalesOrderItem;
import it.verde.view.bean.SalesOrderItemBean;

import java.util.Objects;

public final class SalesOrderItemMapper {
    public SalesOrderItem toEntity(SalesOrderItemBean bean) {
        Objects.requireNonNull(bean, "Sales order item bean cannot be null");
        int quantity = Objects.requireNonNull(bean.getQuantity(), "Quantity cannot be null");
        return new SalesOrderItem(bean.getSpeciesCode(), quantity, bean.getUnitPrice());
    }

    public SalesOrderItemBean toBean(SalesOrderItem entity) {
        Objects.requireNonNull(entity, "Sales order item entity cannot be null");
        SalesOrderItemBean bean = new SalesOrderItemBean();
        bean.setSpeciesCode(entity.getSpeciesCode());
        bean.setQuantity(entity.getQuantity());
        bean.setUnitPrice(entity.getUnitPrice());
        bean.setLineTotal(entity.calculateTotal());
        return bean;
    }
}
