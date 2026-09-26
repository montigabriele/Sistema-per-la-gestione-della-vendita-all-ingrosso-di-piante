package it.verde.model.entity;

import it.verde.model.type.OrderStatus;

import java.time.LocalDate;

public class SupplyOrder extends AbstractOrder<SupplyOrderItem> {
    private final int supplierId;
    private String contactEmail;

    public SupplyOrder(int supplierId) {
        super();
        if (supplierId <= 0) throw new IllegalArgumentException("Supplier id must be greater than zero");
        this.contactEmail = "";
        this.supplierId = supplierId;
    }

    public SupplyOrder(int orderId, LocalDate orderDate, OrderStatus status, int supplierId) {
        this(supplierId);
        restoreState(orderId, orderDate, status);
    }

    @Override
    protected String nullItemMessage() {
        return "Supply order item cannot be null";
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
