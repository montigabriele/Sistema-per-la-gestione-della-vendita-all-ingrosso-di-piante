package it.verde.persistence.dao;

import it.verde.exception.DaoException;
import it.verde.model.entity.PriceHistory;

import java.math.BigDecimal;
import java.util.List;

public interface PriceHistoryDao {
    void insertPriceVariation(String speciesCode, BigDecimal newPrice) throws DaoException;
    List<PriceHistory> findBySpeciesCode(String speciesCode) throws DaoException;
    BigDecimal findCurrentPrice(String speciesCode) throws DaoException;
}
