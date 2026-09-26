package it.verde.model.entity;

import it.verde.model.type.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SalesOrder extends AbstractOrder<SalesOrderItem> {
    private String customerVatNumber;

    public SalesOrder() {
        super();
    }

    public SalesOrder(int orderId, String customerVatNumber, LocalDate orderDate, OrderStatus status) {
        this();
        this.customerVatNumber = customerVatNumber;
        restoreState(orderId, orderDate, status);
    }

    public String getCustomerVatNumber() {
        return customerVatNumber;
    }

    public void setCustomerVatNumber(String customerVatNumber) {
        this.customerVatNumber = customerVatNumber;
    }

    @Override
    protected String nullItemMessage() {
        return "Sales order item cannot be null";
    }

    public BigDecimal calculateTotal() {
        return getItems().stream()
                .map(SalesOrderItem::calculateTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
