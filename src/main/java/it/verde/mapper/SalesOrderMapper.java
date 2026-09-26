package it.verde.mapper;

import it.verde.model.entity.SalesOrder;
import it.verde.model.type.OrderStatus;
import it.verde.view.bean.SalesOrderBean;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

public final class SalesOrderMapper {
    private final SalesOrderItemMapper itemMapper;
    private final Clock clock;

    public SalesOrderMapper() {
        this(new SalesOrderItemMapper(),Clock.systemDefaultZone());
    }

    public SalesOrderMapper(SalesOrderItemMapper itemMapper) {
        this(itemMapper,Clock.systemDefaultZone());
    }

    public SalesOrderMapper(SalesOrderItemMapper itemMapper,Clock clock) {
        this.itemMapper=Objects.requireNonNull(itemMapper);
        this.clock=Objects.requireNonNull(clock);
    }

    public SalesOrder toEntity(SalesOrderBean bean) {
        Objects.requireNonNull(bean,"Sales order bean cannot be null");
        int orderId=bean.getOrderId()==null ? 0 : bean.getOrderId();
        LocalDate orderDate=bean.getOrderDate()==null ? LocalDate.now(clock) : bean.getOrderDate();
        OrderStatus status=bean.getStatus()==null || bean.getStatus().isBlank() ? OrderStatus.OPEN : OrderStatus.valueOf(bean.getStatus());
        SalesOrder entity=new SalesOrder(orderId,bean.getCustomerVatNumber(),orderDate,status);
        copyMutableFields(bean,entity);
        entity.setItems(bean.getItems().stream().map(itemMapper::toEntity).toList());
        return entity;
    }

    public SalesOrder toEntityHeader(SalesOrderBean bean) {
        Objects.requireNonNull(bean,"Sales order bean cannot be null");
        SalesOrder entity=new SalesOrder();
        if(bean.getOrderId()!=null) entity.setOrderId(bean.getOrderId());
        entity.setCustomerVatNumber(bean.getCustomerVatNumber());
        copyMutableFields(bean,entity);
        return entity;
    }

    public SalesOrderBean toBean(SalesOrder entity) {
        Objects.requireNonNull(entity,"Sales order entity cannot be null");
        SalesOrderBean bean=new SalesOrderBean();
        bean.setOrderId(entity.getOrderId());
        bean.setCustomerVatNumber(entity.getCustomerVatNumber());
        bean.setOrderDate(entity.getOrderDate());
        bean.setStatus(entity.getStatus().name());
        bean.setDeliveryStreet(entity.getDeliveryStreet());
        bean.setDeliveryCity(entity.getDeliveryCity());
        bean.setDeliveryPostalCode(entity.getDeliveryPostalCode());
        bean.setContactPerson(entity.getContactPerson());
        bean.setCourierContact(entity.getCourierContact());
        bean.setItems(entity.getItems().stream().map(itemMapper::toBean).toList());
        bean.setTotal(entity.calculateTotal());
        return bean;
    }

    private void copyMutableFields(SalesOrderBean bean,SalesOrder entity) {
        entity.setDeliveryStreet(bean.getDeliveryStreet());
        entity.setDeliveryCity(bean.getDeliveryCity());
        entity.setDeliveryPostalCode(bean.getDeliveryPostalCode());
        entity.setContactPerson(bean.getContactPerson());
        entity.setCourierContact(bean.getCourierContact());
    }
}