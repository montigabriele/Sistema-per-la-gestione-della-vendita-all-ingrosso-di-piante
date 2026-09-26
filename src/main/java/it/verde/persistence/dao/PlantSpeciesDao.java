package it.verde.persistence.dao;

import it.verde.exception.DaoException;
import it.verde.model.entity.PlantSpecies;
import it.verde.model.readmodel.SpeciesCatalogEntry;
import it.verde.model.readmodel.SpeciesSalesReport;
import it.verde.model.type.FlowerColor;
import it.verde.model.type.PlantType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PlantSpeciesDao {
    String createWithInitialPrice(PlantSpecies species, BigDecimal initialPrice) throws DaoException;
    Optional<PlantSpecies> findByCode(String speciesCode) throws DaoException;
    List<PlantSpecies> findAll() throws DaoException;
    void update(PlantSpecies species) throws DaoException;
    boolean deleteByCode(String speciesCode) throws DaoException;
    boolean existsByCode(String speciesCode) throws DaoException;
    List<SpeciesCatalogEntry> findCatalog() throws DaoException;
    List<SpeciesSalesReport> findSalesReport() throws DaoException;
    List<PlantSpecies> findByName(String searchTerm) throws DaoException;
    List<PlantSpecies> findByPlantType(PlantType plantType) throws DaoException;
    List<PlantSpecies> findByFlowerColor(FlowerColor flowerColor) throws DaoException;
}
