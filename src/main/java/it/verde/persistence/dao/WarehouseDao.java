package it.verde.persistence.dao;

import it.verde.exception.DaoException;
import it.verde.model.entity.WarehouseStock;
import it.verde.model.readmodel.CriticalStock;

import java.util.List;

public interface WarehouseDao {
    void create(WarehouseStock stock) throws DaoException;
    void updateQuantity(String speciesCode, int quantity) throws DaoException;
    int findQuantityBySpeciesCode(String speciesCode) throws DaoException;
    List<CriticalStock> findCriticalStocks() throws DaoException;
}
