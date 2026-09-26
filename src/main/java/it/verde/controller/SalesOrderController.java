package it.verde.controller;

import it.verde.exception.ApplicationException;
import it.verde.exception.DaoException;
import it.verde.mapper.SalesOrderItemMapper;
import it.verde.mapper.SalesOrderMapper;
import it.verde.model.entity.SalesOrder;
import it.verde.model.entity.SalesOrderItem;
import it.verde.model.type.OrderStatus;
import it.verde.persistence.dao.PlantSpeciesDao;
import it.verde.persistence.dao.PriceHistoryDao;
import it.verde.persistence.dao.RetailCompanyDao;
import it.verde.persistence.dao.SalesOrderDao;
import it.verde.persistence.dao.WarehouseDao;
import it.verde.view.bean.SalesOrderBean;
import it.verde.view.bean.SalesOrderItemBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class SalesOrderController {
    private final SalesOrderDao salesOrderDao;
    private final RetailCompanyDao retailCompanyDao;
    private final PlantSpeciesDao plantSpeciesDao;
    private final WarehouseDao warehouseDao;
    private final PriceHistoryDao priceHistoryDao;
    private final SalesOrderMapper salesOrderMapper;
    private final SalesOrderItemMapper salesOrderItemMapper;

    public SalesOrderController(SalesOrderDao salesOrderDao, RetailCompanyDao retailCompanyDao, PlantSpeciesDao plantSpeciesDao, WarehouseDao warehouseDao, PriceHistoryDao priceHistoryDao, SalesOrderMapper salesOrderMapper, SalesOrderItemMapper salesOrderItemMapper) {
        this.salesOrderDao = Objects.requireNonNull(salesOrderDao);
        this.retailCompanyDao = Objects.requireNonNull(retailCompanyDao);
        this.plantSpeciesDao = Objects.requireNonNull(plantSpeciesDao);
        this.warehouseDao = Objects.requireNonNull(warehouseDao);
        this.priceHistoryDao = Objects.requireNonNull(priceHistoryDao);
        this.salesOrderMapper = Objects.requireNonNull(salesOrderMapper);
        this.salesOrderItemMapper = Objects.requireNonNull(salesOrderItemMapper);
    }

    public int createOrder(SalesOrderBean bean) throws ApplicationException {
        SalesOrder order = mapAndValidateForCreation(bean);
        ensureCustomerExists(order.getCustomerVatNumber());
        order.setItems(prepareItemsForCreation(bean.getItems()));
        try {
            return salesOrderDao.create(order);
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public List<SalesOrderBean> getAllOrders() throws ApplicationException {
        try {
            return salesOrderDao.findAll().stream().map(salesOrderMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve sales orders", e);
        }
    }

    public Optional<SalesOrderBean> getOrderById(Integer orderId) throws ApplicationException {
        requireOrderId(orderId);
        try {
            return salesOrderDao.findById(orderId).map(salesOrderMapper::toBean);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve sales order", e);
        }
    }

    public void updateOrder(SalesOrderBean bean) throws ApplicationException {
        if (bean == null) throw new ApplicationException("Sales order cannot be null");
        requireOrderId(bean.getOrderId());
        SalesOrder existingOrder = findOrderEntity(bean.getOrderId());
        if (!existingOrder.isEditable()) throw new ApplicationException("Sales order can be modified only when its status is OPEN. Current status: " + existingOrder.getStatus());

        SalesOrder updatedOrder = mapHeader(bean);
        updatedOrder.setOrderId(existingOrder.getOrderId());
        updatedOrder.setCustomerVatNumber(existingOrder.getCustomerVatNumber());
        updatedOrder.setOrderDate(existingOrder.getOrderDate());
        updatedOrder.setItems(existingOrder.getItems());
        if (isBlank(updatedOrder.getDeliveryStreet())) updatedOrder.setDeliveryStreet(existingOrder.getDeliveryStreet());
        if (isBlank(updatedOrder.getDeliveryCity())) updatedOrder.setDeliveryCity(existingOrder.getDeliveryCity());
        if (isBlank(updatedOrder.getDeliveryPostalCode())) updatedOrder.setDeliveryPostalCode(existingOrder.getDeliveryPostalCode());
        if (isBlank(updatedOrder.getContactPerson())) updatedOrder.setContactPerson(existingOrder.getContactPerson());
        if (isBlank(updatedOrder.getCourierContact())) updatedOrder.setCourierContact(existingOrder.getCourierContact());

        try {
            salesOrderDao.update(updatedOrder);
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public boolean deleteOrder(Integer orderId) throws ApplicationException {
        requireOrderId(orderId);
        SalesOrder order = findOrderEntity(orderId);
        if (!order.isEditable()) {
            throw new ApplicationException("Sales order can be deleted only when its status is OPEN. Current status: " + order.getStatus());
        }
        try {
            return salesOrderDao.deleteById(orderId);
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public void changeOrderStatus(Integer orderId, String newStatus) throws ApplicationException {
        requireOrderId(orderId);
        OrderStatus targetStatus = parseStatus(newStatus);
        SalesOrder order = findOrderEntity(orderId);

        try {
            order.changeStatus(targetStatus);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ApplicationException(e.getMessage(), e);
        }

        try {
            if (!salesOrderDao.updateStatus(orderId, targetStatus)) throw new ApplicationException("Unable to change sales order status");
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public List<SalesOrderBean> getOrdersByStatus(String status) throws ApplicationException {
        OrderStatus orderStatus = parseStatus(status);
        try {
            return salesOrderDao.findByStatus(orderStatus).stream().map(salesOrderMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve sales orders by status", e);
        }
    }

    public List<SalesOrderBean> getOrdersByCustomerVatNumber(String vatNumber) throws ApplicationException {
        String normalizedVatNumber = normalizeVatNumber(vatNumber);
        try {
            return salesOrderDao.findByCustomerVatNumber(normalizedVatNumber).stream().map(salesOrderMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve sales orders by customer", e);
        }
    }

    public boolean isAvailable(String speciesCode, int quantity) throws ApplicationException {
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        if (quantity <= 0) throw new ApplicationException("Quantity must be greater than zero");
        try {
            return warehouseDao.findQuantityBySpeciesCode(normalizedCode) >= quantity;
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify warehouse availability", e);
        }
    }

    public int getAvailableQuantity(String speciesCode) throws ApplicationException {
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        try {
            return warehouseDao.findQuantityBySpeciesCode(normalizedCode);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve warehouse availability", e);
        }
    }

    public BigDecimal getCurrentPrice(String speciesCode) throws ApplicationException {
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        ensureSpeciesExists(normalizedCode);
        try {
            return priceHistoryDao.findCurrentPrice(normalizedCode);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve current price", e);
        }
    }

    public SalesOrderItemBean createOrderItem(String speciesCode, int quantity) throws ApplicationException {
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        if (quantity <= 0) throw new ApplicationException("Quantity must be greater than zero");
        ensureSpeciesExists(normalizedCode);
        int availableQuantity = getAvailableQuantity(normalizedCode);
        if (availableQuantity < quantity) throw new ApplicationException("Requested quantity is not available for species " + normalizedCode);

        BigDecimal unitPrice = getCurrentPrice(normalizedCode);
        SalesOrderItem item = new SalesOrderItem(normalizedCode, quantity, unitPrice);
        return salesOrderItemMapper.toBean(item);
    }

    public boolean existsById(Integer orderId) throws ApplicationException {
        requireOrderId(orderId);
        try {
            return salesOrderDao.existsById(orderId);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify sales order", e);
        }
    }

    private SalesOrder mapAndValidateForCreation(SalesOrderBean bean) throws ApplicationException {
        SalesOrder order = mapHeader(bean);
        if (isBlank(order.getCustomerVatNumber())) throw new ApplicationException("Customer VAT number is required");
        if (bean.getItems() == null || bean.getItems().isEmpty()) throw new ApplicationException("A sales order must contain at least one item");
        if (isBlank(order.getDeliveryStreet()) || isBlank(order.getDeliveryCity()) || isBlank(order.getDeliveryPostalCode()) || isBlank(order.getContactPerson()) || isBlank(order.getCourierContact())) throw new ApplicationException("All sales order fields are required");
        return order;
    }

    private SalesOrder mapHeader(SalesOrderBean bean) throws ApplicationException {
        if (bean == null) throw new ApplicationException("Sales order cannot be null");
        try {
            return salesOrderMapper.toEntityHeader(bean);
        } catch (RuntimeException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    private SalesOrder findOrderEntity(int orderId) throws ApplicationException {
        try {
            return salesOrderDao.findById(orderId).orElseThrow(() -> new ApplicationException("Sales order not found with id: " + orderId));
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve sales order", e);
        }
    }

    private void ensureCustomerExists(String vatNumber) throws ApplicationException {
        try {
            if (!retailCompanyDao.existsByVatNumber(vatNumber)) throw new ApplicationException("Customer with VAT number " + vatNumber + " not found");
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify customer", e);
        }
    }

    private void ensureSpeciesExists(String speciesCode) throws ApplicationException {
        try {
            if (!plantSpeciesDao.existsByCode(speciesCode)) throw new ApplicationException("Plant species not found: " + speciesCode);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify plant species", e);
        }
    }

    private List<SalesOrderItem> prepareItemsForCreation(List<SalesOrderItemBean> itemBeans) throws ApplicationException {
        java.util.ArrayList<SalesOrderItem> items = new java.util.ArrayList<>();
        java.util.HashSet<String> speciesCodes = new java.util.HashSet<>();
        for (SalesOrderItemBean itemBean : itemBeans) {
            if (itemBean == null) throw new ApplicationException("Sales order item cannot be null");
            String speciesCode = normalizeSpeciesCode(itemBean.getSpeciesCode());
            if (!speciesCodes.add(speciesCode)) {
                throw new ApplicationException("Plant species " + speciesCode + " appears more than once in the sales order");
            }
            Integer quantity = itemBean.getQuantity();
            if (quantity == null || quantity <= 0) throw new ApplicationException("Quantity must be greater than zero for species " + speciesCode);
            ensureSpeciesExists(speciesCode);
            if (!isAvailable(speciesCode, quantity)) throw new ApplicationException("Requested quantity is not available for species " + speciesCode);
            items.add(new SalesOrderItem(speciesCode, quantity, getCurrentPrice(speciesCode)));
        }
        return items;
    }

    private void requireOrderId(Integer orderId) throws ApplicationException {
        if (orderId == null || orderId <= 0) throw new ApplicationException("Invalid sales order id");
    }

    private String normalizeSpeciesCode(String speciesCode) throws ApplicationException {
        if (speciesCode == null || speciesCode.trim().isEmpty()) throw new ApplicationException("Species code is required");
        return speciesCode.trim().toUpperCase();
    }

    private String normalizeVatNumber(String vatNumber) throws ApplicationException {
        if (vatNumber == null || vatNumber.trim().isEmpty()) throw new ApplicationException("Customer VAT number is required");
        String normalized = vatNumber.trim();
        if (!normalized.matches("^\\d{11}$")) throw new ApplicationException("VAT number must contain 11 digits");
        return normalized;
    }

    private OrderStatus parseStatus(String status) throws ApplicationException {
        if (status == null || status.trim().isEmpty()) throw new ApplicationException("Order status is required");
        try {
            return OrderStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApplicationException("Invalid order status: " + status, e);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
