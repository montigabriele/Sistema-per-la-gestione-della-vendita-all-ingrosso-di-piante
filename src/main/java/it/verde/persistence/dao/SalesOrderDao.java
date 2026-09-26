package it.verde.persistence.dao;

import it.verde.exception.DaoException;
import it.verde.model.entity.SalesOrder;
import it.verde.model.type.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface SalesOrderDao {
    Integer create(SalesOrder order) throws DaoException;
    Optional<SalesOrder> findById(int orderId) throws DaoException;
    List<SalesOrder> findAll() throws DaoException;
    void update(SalesOrder order) throws DaoException;
    boolean deleteById(int orderId) throws DaoException;
    boolean existsById(int orderId) throws DaoException;
    boolean updateStatus(int orderId, OrderStatus status) throws DaoException;
    List<SalesOrder> findByStatus(OrderStatus status) throws DaoException;
    List<SalesOrder> findByCustomerVatNumber(String vatNumber) throws DaoException;
}
