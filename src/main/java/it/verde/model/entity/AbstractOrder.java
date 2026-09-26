package it.verde.model.entity;

import it.verde.model.type.OrderStatus;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


abstract class AbstractOrder<I> {
    private int orderId;
    private LocalDate orderDate;
    private OrderStatus status;
    private final FulfillmentDetails fulfillmentDetails = new FulfillmentDetails();
    private List<I> items;

    protected AbstractOrder() {
        this.items = new ArrayList<>();
        this.orderDate = LocalDate.now(ZoneId.systemDefault());
        this.status = OrderStatus.OPEN;
    }

    protected final void restoreState(int orderId, LocalDate orderDate, OrderStatus status) {
        this.orderId = orderId;
        this.orderDate = Objects.requireNonNull(orderDate, "Order date cannot be null");
        this.status = Objects.requireNonNull(status, "Order status cannot be null");
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = Objects.requireNonNull(orderDate, "Order date cannot be null");
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getDeliveryStreet() {
        return fulfillmentDetails.deliveryStreet;
    }

    public void setDeliveryStreet(String deliveryStreet) {
        fulfillmentDetails.deliveryStreet = deliveryStreet;
    }

    public String getDeliveryCity() {
        return fulfillmentDetails.deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        fulfillmentDetails.deliveryCity = deliveryCity;
    }

    public String getDeliveryPostalCode() {
        return fulfillmentDetails.deliveryPostalCode;
    }

    public void setDeliveryPostalCode(String deliveryPostalCode) {
        fulfillmentDetails.deliveryPostalCode = deliveryPostalCode;
    }

    public String getContactPerson() {
        return fulfillmentDetails.contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        fulfillmentDetails.contactPerson = contactPerson;
    }

    public String getCourierContact() {
        return fulfillmentDetails.courierContact;
    }

    public void setCourierContact(String courierContact) {
        fulfillmentDetails.courierContact = courierContact;
    }

    public List<I> getItems() {
        return List.copyOf(items);
    }

    public void setItems(List<I> items) {
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
    }

    public void addItem(I item) {
        items.add(Objects.requireNonNull(item, nullItemMessage()));
    }

    protected abstract String nullItemMessage();

    public boolean isEditable() {
        return status == OrderStatus.OPEN;
    }

    public void changeStatus(OrderStatus newStatus) {
        if (newStatus == null) throw new IllegalArgumentException("Order status cannot be null");
        if (!status.canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid order status transition from " + status + " to " + newStatus);
        }
        status = newStatus;
    }

    private static final class FulfillmentDetails {
        private String deliveryStreet;
        private String deliveryCity;
        private String deliveryPostalCode;
        private String contactPerson;
        private String courierContact;
    }
}
