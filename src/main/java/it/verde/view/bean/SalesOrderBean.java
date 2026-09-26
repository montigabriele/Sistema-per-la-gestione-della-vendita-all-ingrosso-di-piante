package it.verde.view.bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalesOrderBean {
    private Integer orderId;
    private String customerVatNumber;
    private LocalDate orderDate;
    private String status;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryPostalCode;
    private String contactPerson;
    private String courierContact;
    private List<SalesOrderItemBean> items = new ArrayList<>();
    private BigDecimal total;

    public SalesOrderBean() {
        // empty
    }

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public String getCustomerVatNumber() { return customerVatNumber; }
    public void setCustomerVatNumber(String customerVatNumber) { this.customerVatNumber = validateVatNumber(customerVatNumber); }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = validateStatus(status); }
    public String getDeliveryStreet() { return deliveryStreet; }
    public void setDeliveryStreet(String deliveryStreet) { this.deliveryStreet = deliveryStreet; }
    public String getDeliveryCity() { return deliveryCity; }
    public void setDeliveryCity(String deliveryCity) { this.deliveryCity = deliveryCity; }
    public String getDeliveryPostalCode() { return deliveryPostalCode; }
    public void setDeliveryPostalCode(String deliveryPostalCode) { this.deliveryPostalCode = validatePostalCode(deliveryPostalCode); }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getCourierContact() { return courierContact; }
    public void setCourierContact(String courierContact) { this.courierContact = validatePhone(courierContact); }
    public List<SalesOrderItemBean> getItems() { return items; }
    public void setItems(List<SalesOrderItemBean> items) { this.items = items != null ? items : new ArrayList<>(); }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    private String validateVatNumber(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim();
        if (!normalized.matches("^\\d{11}$")) throw new IllegalArgumentException("VAT number must contain 11 digits");
        return normalized;
    }

    private String validatePostalCode(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim();
        if (!normalized.matches("^\\d{5}$")) throw new IllegalArgumentException("Postal code must contain 5 digits");
        return normalized;
    }

    private String validateStatus(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim().toUpperCase();
        if (!normalized.matches("OPEN|CONFIRMED|SHIPPED|DELIVERED|CANCELLED")) throw new IllegalArgumentException("Invalid order status");
        return normalized;
    }

    private String validatePhone(String value) {
        if (value == null || value.isBlank()) return value;
        String normalized = value.trim();
        if (!normalized.matches("^[+]?\\d{8,15}$")) throw new IllegalArgumentException("Invalid phone format");
        return normalized;
    }
}
