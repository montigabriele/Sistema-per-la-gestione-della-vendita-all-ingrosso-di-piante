package it.verde.controller;

import it.verde.exception.ApplicationException;
import it.verde.exception.DaoException;
import it.verde.mapper.PlantSpeciesMapper;
import it.verde.mapper.SupplierMapper;
import it.verde.mapper.SupplyOrderItemMapper;
import it.verde.mapper.SupplyOrderMapper;
import it.verde.model.entity.SupplyOrder;
import it.verde.model.entity.SupplyOrderItem;
import it.verde.model.type.OrderStatus;
import it.verde.persistence.dao.PlantSpeciesDao;
import it.verde.persistence.dao.SupplierDao;
import it.verde.persistence.dao.SupplyOrderDao;
import it.verde.persistence.dao.WarehouseDao;
import it.verde.view.bean.PlantSpeciesBean;
import it.verde.view.bean.SupplierBean;
import it.verde.view.bean.SupplyOrderBean;
import it.verde.view.bean.SupplyOrderItemBean;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class SupplyOrderController {
    private final SupplyOrderDao supplyOrderDao;
    private final SupplierDao supplierDao;
    private final PlantSpeciesDao plantSpeciesDao;
    private final WarehouseDao warehouseDao;
    private final SupplyOrderMapper supplyOrderMapper;
    private final SupplyOrderItemMapper supplyOrderItemMapper;
    private final SupplierMapper supplierMapper;
    private final PlantSpeciesMapper plantSpeciesMapper;

    public record DaoDependencies( SupplyOrderDao supplyOrderDao, SupplierDao supplierDao, PlantSpeciesDao plantSpeciesDao, WarehouseDao warehouseDao) {}

    public record MapperDependencies( SupplyOrderMapper supplyOrderMapper, SupplyOrderItemMapper supplyOrderItemMapper, SupplierMapper supplierMapper, PlantSpeciesMapper plantSpeciesMapper) {}

    public SupplyOrderController(DaoDependencies daos, MapperDependencies mappers) {
        Objects.requireNonNull(daos);
        Objects.requireNonNull(mappers);
        this.supplyOrderDao = Objects.requireNonNull(daos.supplyOrderDao());
        this.supplierDao = Objects.requireNonNull(daos.supplierDao());
        this.plantSpeciesDao = Objects.requireNonNull(daos.plantSpeciesDao());
        this.warehouseDao = Objects.requireNonNull(daos.warehouseDao());
        this.supplyOrderMapper = Objects.requireNonNull(mappers.supplyOrderMapper());
        this.supplyOrderItemMapper = Objects.requireNonNull(mappers.supplyOrderItemMapper());
        this.supplierMapper = Objects.requireNonNull(mappers.supplierMapper());
        this.plantSpeciesMapper = Objects.requireNonNull(mappers.plantSpeciesMapper());
    }

    public int createOrder(SupplyOrderBean bean) throws ApplicationException {
        SupplyOrder order = mapAndValidateForCreation(bean);
        ensureSupplierExists(order.getSupplierId());
        validateItemsForSupplier(order.getSupplierId(), order.getItems());

        try {
            return supplyOrderDao.create(order);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to create supply order", e);
        }
    }

    public List<SupplyOrderBean> getAllOrders() throws ApplicationException {
        try {
            return supplyOrderDao.findAll().stream().map(supplyOrderMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve supply orders", e);
        }
    }

    public Optional<SupplyOrderBean> getOrderById(Integer orderId) throws ApplicationException {
        requireOrderId(orderId);
        try {
            return supplyOrderDao.findById(orderId).map(supplyOrderMapper::toBean);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve supply order", e);
        }
    }

    public void updateOrder(SupplyOrderBean bean) throws ApplicationException {
        if (bean == null) throw new ApplicationException("Supply order cannot be null");
        requireOrderId(bean.getOrderId());
        SupplyOrder existingOrder = findOrderEntity(bean.getOrderId());
        if (!existingOrder.isEditable()) throw new ApplicationException("Supply order can be modified only when its status is OPEN. Current status: " + existingOrder.getStatus());

        SupplyOrder updatedOrder = mapHeader(bean);
        updatedOrder.setOrderId(existingOrder.getOrderId());
        updatedOrder.setOrderDate(existingOrder.getOrderDate());
        updatedOrder.setItems(existingOrder.getItems());
        if (isBlank(updatedOrder.getContactEmail())) updatedOrder.setContactEmail(existingOrder.getContactEmail());
        if (isBlank(updatedOrder.getDeliveryStreet())) updatedOrder.setDeliveryStreet(existingOrder.getDeliveryStreet());
        if (isBlank(updatedOrder.getDeliveryCity())) updatedOrder.setDeliveryCity(existingOrder.getDeliveryCity());
        if (isBlank(updatedOrder.getDeliveryPostalCode())) updatedOrder.setDeliveryPostalCode(existingOrder.getDeliveryPostalCode());
        if (isBlank(updatedOrder.getContactPerson())) updatedOrder.setContactPerson(existingOrder.getContactPerson());
        if (isBlank(updatedOrder.getCourierContact())) updatedOrder.setCourierContact(existingOrder.getCourierContact());
        validateRequiredFields(updatedOrder);

        try {
            supplyOrderDao.update(updatedOrder);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to update supply order", e);
        }
    }

    public boolean deleteOrder(Integer orderId) throws ApplicationException {
        requireOrderId(orderId);
        SupplyOrder order = findOrderEntity(orderId);
        if (!order.isEditable()) throw new ApplicationException("Supply order can be deleted only when its status is OPEN. Current status: " + order.getStatus());

        try {
            return supplyOrderDao.deleteById(orderId);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to delete supply order", e);
        }
    }

    public void changeOrderStatus(Integer orderId, String newStatus) throws ApplicationException {
        requireOrderId(orderId);
        OrderStatus targetStatus = parseStatus(newStatus);
        SupplyOrder order = findOrderEntity(orderId);

        try {
            order.changeStatus(targetStatus);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ApplicationException(e.getMessage(), e);
        }

        try {
            if (!supplyOrderDao.updateStatus(orderId, targetStatus)) throw new ApplicationException("Unable to change supply order status");
        } catch (DaoException e) {
            throw new ApplicationException("Unable to change supply order status", e);
        }
    }

    public List<SupplyOrderBean> getOrdersByStatus(String status) throws ApplicationException {
        OrderStatus orderStatus = parseStatus(status);
        try {
            return supplyOrderDao.findByStatus(orderStatus).stream().map(supplyOrderMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve supply orders by status", e);
        }
    }

    public List<SupplyOrderBean> getOrdersBySupplier(Integer supplierId) throws ApplicationException {
        requireSupplierId(supplierId);
        ensureSupplierExists(supplierId);
        try {
            return supplyOrderDao.findBySupplierId(supplierId).stream().map(supplyOrderMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve supply orders by supplier", e);
        }
    }

    public List<SupplierBean> getAvailableSuppliers() throws ApplicationException {
        try {
            return supplierDao.findAll().stream().map(supplierMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve suppliers", e);
        }
    }

    public Optional<SupplierBean> getSupplierById(Integer supplierId) throws ApplicationException {
        requireSupplierId(supplierId);
        try {
            return supplierDao.findById(supplierId).map(supplierMapper::toBean);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve supplier", e);
        }
    }

    public List<PlantSpeciesBean> getSuppliedSpecies(Integer supplierId) throws ApplicationException {
        requireSupplierId(supplierId);
        ensureSupplierExists(supplierId);
        try {
            return supplierDao.findSuppliedSpecies(supplierId).stream().map(plantSpeciesMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve species supplied by supplier", e);
        }
    }

    public SupplyOrderItemBean createOrderItem(Integer supplierId, String speciesCode, int quantity) throws ApplicationException {
        requireSupplierId(supplierId);
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        if (quantity <= 0) throw new ApplicationException("Quantity must be greater than zero");
        ensureSupplierExists(supplierId);
        ensureSpeciesExists(normalizedCode);
        ensureSupplierProvidesSpecies(supplierId, normalizedCode);
        return supplyOrderItemMapper.toBean(new SupplyOrderItem(normalizedCode, quantity));
    }

    public int getAvailableQuantity(String speciesCode) throws ApplicationException {
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        ensureSpeciesExists(normalizedCode);
        try {
            return warehouseDao.findQuantityBySpeciesCode(normalizedCode);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve warehouse quantity", e);
        }
    }

    public boolean existsById(Integer orderId) throws ApplicationException {
        requireOrderId(orderId);
        try {
            return supplyOrderDao.existsById(orderId);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify supply order", e);
        }
    }

    private SupplyOrder mapAndValidateForCreation(SupplyOrderBean bean) throws ApplicationException {
        SupplyOrder order = mapHeader(bean);
        validateRequiredFields(order);
        if (bean.getItems() == null || bean.getItems().isEmpty()) throw new ApplicationException("A supply order must contain at least one item");
        try {
            order.setItems(bean.getItems().stream().map(supplyOrderItemMapper::toEntity).toList());
        } catch (RuntimeException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
        return order;
    }

    private SupplyOrder mapHeader(SupplyOrderBean bean) throws ApplicationException {
        if (bean == null) throw new ApplicationException("Supply order cannot be null");
        try {
            return supplyOrderMapper.toEntityHeader(bean);
        } catch (RuntimeException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    private SupplyOrder findOrderEntity(int orderId) throws ApplicationException {
        try {
            return supplyOrderDao.findById(orderId).orElseThrow(() -> new ApplicationException("Supply order not found with id: " + orderId));
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve supply order", e);
        }
    }

    private void ensureSupplierExists(int supplierId) throws ApplicationException {
        try {
            if (!supplierDao.existsById(supplierId)) throw new ApplicationException("Supplier not found with id: " + supplierId);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify supplier", e);
        }
    }

    private void ensureSpeciesExists(String speciesCode) throws ApplicationException {
        try {
            if (!plantSpeciesDao.existsByCode(speciesCode)) throw new ApplicationException("Plant species not found: " + speciesCode);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify plant species", e);
        }
    }

    private void ensureSupplierProvidesSpecies(int supplierId, String speciesCode) throws ApplicationException {
        try {
            if (!supplierDao.suppliesSpecies(supplierId, speciesCode)) throw new ApplicationException("Supplier " + supplierId + " does not supply species " + speciesCode);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify supplier species", e);
        }
    }

    private void validateItemsForSupplier(int supplierId, List<SupplyOrderItem> items) throws ApplicationException {
        java.util.HashSet<String> speciesCodes = new java.util.HashSet<>();
        for (SupplyOrderItem item : items) {
            String speciesCode = normalizeSpeciesCode(item.getSpeciesCode());
            if (!speciesCodes.add(speciesCode)) {
                throw new ApplicationException("Plant species " + speciesCode + " appears more than once in the supply order");
            }
            if (item.getQuantity() <= 0) throw new ApplicationException("Quantity must be greater than zero for species " + speciesCode);
            ensureSpeciesExists(speciesCode);
            ensureSupplierProvidesSpecies(supplierId, speciesCode);
        }
    }

    private void validateRequiredFields(SupplyOrder order) throws ApplicationException {
        if (order.getSupplierId() <= 0) throw new ApplicationException("Invalid supplier id");
        if (isBlank(order.getContactEmail())) throw new ApplicationException("Contact email is required");
        if (isBlank(order.getDeliveryStreet()) || isBlank(order.getDeliveryCity()) || isBlank(order.getDeliveryPostalCode()) || isBlank(order.getContactPerson()) || isBlank(order.getCourierContact())) throw new ApplicationException("All supply order fields are required");
    }

    private void requireOrderId(Integer orderId) throws ApplicationException {
        if (orderId == null || orderId <= 0) throw new ApplicationException("Invalid supply order id");
    }

    private void requireSupplierId(Integer supplierId) throws ApplicationException {
        if (supplierId == null || supplierId <= 0) throw new ApplicationException("Invalid supplier id");
    }

    private String normalizeSpeciesCode(String speciesCode) throws ApplicationException {
        if (speciesCode == null || speciesCode.trim().isEmpty()) throw new ApplicationException("Species code is required");
        return speciesCode.trim().toUpperCase();
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
