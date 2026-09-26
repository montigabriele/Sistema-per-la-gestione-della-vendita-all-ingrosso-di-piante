package it.verde.persistence.dao;

import it.verde.exception.DaoException;
import it.verde.model.entity.PlantSpecies;
import it.verde.model.entity.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierDao {
    Integer create(Supplier supplier) throws DaoException;
    Optional<Supplier> findById(Integer supplierId) throws DaoException;
    List<Supplier> findAll() throws DaoException;
    void update(Supplier supplier) throws DaoException;
    boolean deleteById(Integer supplierId) throws DaoException;
    boolean existsById(Integer supplierId) throws DaoException;
    void addSuppliedSpecies(Integer supplierId, String speciesCode) throws DaoException;
    void removeSuppliedSpecies(Integer supplierId, String speciesCode) throws DaoException;
    List<String> findSuppliedSpeciesCodes(Integer supplierId) throws DaoException;
    boolean suppliesSpecies(Integer supplierId, String speciesCode) throws DaoException;
    List<PlantSpecies> findSuppliedSpecies(Integer supplierId) throws DaoException;
}
