package it.verde.persistence.dao;

import it.verde.exception.DaoException;
import it.verde.model.entity.SupplyOrder;
import it.verde.model.type.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface SupplyOrderDao {
    Integer create(SupplyOrder order) throws DaoException;
    Optional<SupplyOrder> findById(int orderId) throws DaoException;
    List<SupplyOrder> findAll() throws DaoException;
    void update(SupplyOrder order) throws DaoException;
    boolean deleteById(int orderId) throws DaoException;
    boolean existsById(int orderId) throws DaoException;
    boolean updateStatus(int orderId, OrderStatus status) throws DaoException;
    List<SupplyOrder> findByStatus(OrderStatus status) throws DaoException;
    List<SupplyOrder> findBySupplierId(int supplierId) throws DaoException;
}
