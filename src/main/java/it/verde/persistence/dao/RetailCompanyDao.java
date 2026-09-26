package it.verde.persistence.dao;

import it.verde.exception.DaoException;
import it.verde.model.entity.RetailCompany;

import java.util.List;
import java.util.Optional;

public interface RetailCompanyDao {
    String create(RetailCompany company) throws DaoException;
    Optional<RetailCompany> findByVatNumber(String vatNumber) throws DaoException;
    List<RetailCompany> findAll() throws DaoException;
    void update(RetailCompany company) throws DaoException;
    boolean deleteByVatNumber(String vatNumber) throws DaoException;
    boolean existsByVatNumber(String vatNumber) throws DaoException;
}
