package it.verde.mapper;

import it.verde.model.entity.SupplyOrder;
import it.verde.model.type.OrderStatus;
import it.verde.view.bean.SupplyOrderBean;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

public final class SupplyOrderMapper {
    private final SupplyOrderItemMapper itemMapper;
    private final Clock clock;

    public SupplyOrderMapper() {
        this(new SupplyOrderItemMapper(),Clock.systemDefaultZone());
    }

    public SupplyOrderMapper(SupplyOrderItemMapper itemMapper) {
        this(itemMapper,Clock.systemDefaultZone());
    }

    public SupplyOrderMapper(SupplyOrderItemMapper itemMapper,Clock clock) {
        this.itemMapper=Objects.requireNonNull(itemMapper);
        this.clock=Objects.requireNonNull(clock);
    }

    public SupplyOrder toEntity(SupplyOrderBean bean) {
        Objects.requireNonNull(bean,"Supply order bean cannot be null");
        int supplierId=Objects.requireNonNull(bean.getSupplierId(),"Supplier id cannot be null");
        int orderId=bean.getOrderId()==null ? 0 : bean.getOrderId();
        LocalDate orderDate=bean.getOrderDate()==null ? LocalDate.now(clock) : bean.getOrderDate();
        OrderStatus status=bean.getStatus()==null || bean.getStatus().isBlank() ? OrderStatus.OPEN : OrderStatus.valueOf(bean.getStatus());
        SupplyOrder entity=new SupplyOrder(orderId,orderDate,status,supplierId);
        copyMutableFields(bean,entity);
        entity.setItems(bean.getItems().stream().map(itemMapper::toEntity).toList());
        return entity;
    }

    public SupplyOrder toEntityHeader(SupplyOrderBean bean) {
        Objects.requireNonNull(bean,"Supply order bean cannot be null");
        int supplierId=Objects.requireNonNull(bean.getSupplierId(),"Supplier id cannot be null");
        SupplyOrder entity=new SupplyOrder(supplierId);
        if(bean.getOrderId()!=null) entity.setOrderId(bean.getOrderId());
        copyMutableFields(bean,entity);
        return entity;
    }

    public SupplyOrderBean toBean(SupplyOrder entity) {
        Objects.requireNonNull(entity,"Supply order entity cannot be null");
        SupplyOrderBean bean=new SupplyOrderBean();
        bean.setOrderId(entity.getOrderId());
        bean.setOrderDate(entity.getOrderDate());
        bean.setStatus(entity.getStatus().name());
        bean.setSupplierId(entity.getSupplierId());
        bean.setContactEmail(entity.getContactEmail());
        bean.setDeliveryStreet(entity.getDeliveryStreet());
        bean.setDeliveryCity(entity.getDeliveryCity());
        bean.setDeliveryPostalCode(entity.getDeliveryPostalCode());
        bean.setContactPerson(entity.getContactPerson());
        bean.setCourierContact(entity.getCourierContact());
        bean.setItems(entity.getItems().stream().map(itemMapper::toBean).toList());
        return bean;
    }

    private void copyMutableFields(SupplyOrderBean bean,SupplyOrder entity) {
        entity.setContactEmail(bean.getContactEmail());
        entity.setDeliveryStreet(bean.getDeliveryStreet());
        entity.setDeliveryCity(bean.getDeliveryCity());
        entity.setDeliveryPostalCode(bean.getDeliveryPostalCode());
        entity.setContactPerson(bean.getContactPerson());
        entity.setCourierContact(bean.getCourierContact());
    }
}