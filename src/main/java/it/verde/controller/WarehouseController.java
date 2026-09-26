package it.verde.controller;

import it.verde.exception.ApplicationException;
import it.verde.exception.DaoException;
import it.verde.mapper.CriticalStockMapper;
import it.verde.mapper.SpeciesCatalogItemMapper;
import it.verde.mapper.WarehouseStockMapper;
import it.verde.model.entity.WarehouseStock;
import it.verde.persistence.dao.PlantSpeciesDao;
import it.verde.persistence.dao.WarehouseDao;
import it.verde.view.bean.CriticalStockBean;
import it.verde.view.bean.SpeciesCatalogItemBean;
import it.verde.view.bean.WarehouseStockBean;

import java.util.List;
import java.util.Objects;

public final class WarehouseController {
    private final WarehouseDao warehouseDao;
    private final PlantSpeciesDao plantSpeciesDao;
    private final WarehouseStockMapper warehouseStockMapper;
    private final SpeciesCatalogItemMapper catalogItemMapper;
    private final CriticalStockMapper criticalStockMapper;

    public WarehouseController(WarehouseDao warehouseDao, PlantSpeciesDao plantSpeciesDao, WarehouseStockMapper warehouseStockMapper, SpeciesCatalogItemMapper catalogItemMapper, CriticalStockMapper criticalStockMapper) {
        this.warehouseDao = Objects.requireNonNull(warehouseDao);
        this.plantSpeciesDao = Objects.requireNonNull(plantSpeciesDao);
        this.warehouseStockMapper = Objects.requireNonNull(warehouseStockMapper);
        this.catalogItemMapper = Objects.requireNonNull(catalogItemMapper);
        this.criticalStockMapper = Objects.requireNonNull(criticalStockMapper);
    }

    public void createStock(WarehouseStockBean bean) throws ApplicationException {
        WarehouseStock stock = mapAndValidate(bean);
        if (!speciesExists(stock.getSpeciesCode())) throw new ApplicationException("Invalid species code: plant species does not exist");
        try {
            warehouseDao.create(stock);
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public void updateStock(WarehouseStockBean bean) throws ApplicationException {
        WarehouseStock stock = mapAndValidate(bean);
        if (!speciesExists(stock.getSpeciesCode())) throw new ApplicationException("Invalid species code: plant species does not exist");
        try {
            warehouseDao.updateQuantity(stock.getSpeciesCode(), stock.getQuantity());
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public int getQuantity(String speciesCode) throws ApplicationException {
        validateSpeciesCode(speciesCode);
        try {
            return warehouseDao.findQuantityBySpeciesCode(speciesCode.trim().toUpperCase());
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve warehouse quantity", e);
        }
    }

    public List<SpeciesCatalogItemBean> getWarehouseOverview() throws ApplicationException {
        try {
            return plantSpeciesDao.findCatalog().stream().map(catalogItemMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve warehouse stock", e);
        }
    }

    public List<CriticalStockBean> getCriticalStocks() throws ApplicationException {
        try {
            return warehouseDao.findCriticalStocks().stream().map(criticalStockMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve critical stock", e);
        }
    }

    private WarehouseStock mapAndValidate(WarehouseStockBean bean) throws ApplicationException {
        if (bean == null) throw new ApplicationException("Warehouse stock cannot be null");
        try {
            WarehouseStock stock = warehouseStockMapper.toEntity(bean);
            validateSpeciesCode(stock.getSpeciesCode());
            if (stock.getQuantity() < 0) throw new ApplicationException("Quantity cannot be negative");
            return stock;
        } catch (ApplicationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    private boolean speciesExists(String speciesCode) throws ApplicationException {
        try {
            return plantSpeciesDao.existsByCode(speciesCode.trim().toUpperCase());
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify plant species", e);
        }
    }

    private void validateSpeciesCode(String speciesCode) throws ApplicationException {
        if (speciesCode == null || speciesCode.trim().isEmpty()) throw new ApplicationException("Species code is required");
    }
}
