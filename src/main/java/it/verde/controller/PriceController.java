package it.verde.controller;

import it.verde.exception.ApplicationException;
import it.verde.exception.DaoException;
import it.verde.mapper.PriceHistoryMapper;
import it.verde.persistence.dao.PlantSpeciesDao;
import it.verde.persistence.dao.PriceHistoryDao;
import it.verde.view.bean.PriceHistoryBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public final class PriceController {
    private final PriceHistoryDao priceHistoryDao;
    private final PlantSpeciesDao plantSpeciesDao;
    private final PriceHistoryMapper priceHistoryMapper;

    public PriceController(PriceHistoryDao priceHistoryDao, PlantSpeciesDao plantSpeciesDao, PriceHistoryMapper priceHistoryMapper) {
        this.priceHistoryDao = Objects.requireNonNull(priceHistoryDao);
        this.plantSpeciesDao = Objects.requireNonNull(plantSpeciesDao);
        this.priceHistoryMapper = Objects.requireNonNull(priceHistoryMapper);
    }

    public void changePrice(String speciesCode, BigDecimal newPrice) throws ApplicationException {
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        validatePrice(newPrice);
        ensureSpeciesExists(normalizedCode);

        try {
            priceHistoryDao.insertPriceVariation(normalizedCode, newPrice);
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public List<PriceHistoryBean> getPriceHistory(String speciesCode) throws ApplicationException {
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        ensureSpeciesExists(normalizedCode);

        try {
            return priceHistoryDao.findBySpeciesCode(normalizedCode).stream().map(priceHistoryMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve price history", e);
        }
    }

    public BigDecimal getCurrentPrice(String speciesCode) throws ApplicationException {
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        ensureSpeciesExists(normalizedCode);
        try {
            return priceHistoryDao.findCurrentPrice(normalizedCode);
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    private void ensureSpeciesExists(String speciesCode) throws ApplicationException {
        try {
            if (!plantSpeciesDao.existsByCode(speciesCode)) throw new ApplicationException("Plant species not found: " + speciesCode);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify plant species", e);
        }
    }

    private String normalizeSpeciesCode(String speciesCode) throws ApplicationException {
        if (speciesCode == null || speciesCode.trim().isEmpty()) throw new ApplicationException("Species code is required");
        return speciesCode.trim().toUpperCase();
    }

    private void validatePrice(BigDecimal price) throws ApplicationException {
        if (price == null) throw new ApplicationException("Price is required");
        if (price.compareTo(BigDecimal.ZERO) <= 0) throw new ApplicationException("Price must be greater than zero");
    }
}
