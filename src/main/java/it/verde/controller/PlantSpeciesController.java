package it.verde.controller;

import it.verde.exception.ApplicationException;
import it.verde.exception.DaoException;
import it.verde.mapper.PlantSpeciesMapper;
import it.verde.mapper.SpeciesCatalogItemMapper;
import it.verde.mapper.SpeciesSalesReportMapper;
import it.verde.model.entity.PlantSpecies;
import it.verde.model.type.FlowerColor;
import it.verde.model.type.PlantType;
import it.verde.persistence.dao.PlantSpeciesDao;
import it.verde.view.bean.PlantSpeciesBean;
import it.verde.view.bean.SpeciesCatalogItemBean;
import it.verde.view.bean.SpeciesSalesReportBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class PlantSpeciesController {
    private final PlantSpeciesDao plantSpeciesDao;
    private final PlantSpeciesMapper plantSpeciesMapper;
    private final SpeciesCatalogItemMapper catalogItemMapper;
    private final SpeciesSalesReportMapper salesReportMapper;

    public PlantSpeciesController(PlantSpeciesDao plantSpeciesDao, PlantSpeciesMapper plantSpeciesMapper, SpeciesCatalogItemMapper catalogItemMapper, SpeciesSalesReportMapper salesReportMapper) {
        this.plantSpeciesDao = Objects.requireNonNull(plantSpeciesDao);
        this.plantSpeciesMapper = Objects.requireNonNull(plantSpeciesMapper);
        this.catalogItemMapper = Objects.requireNonNull(catalogItemMapper);
        this.salesReportMapper = Objects.requireNonNull(salesReportMapper);
    }

    public String createSpecies(PlantSpeciesBean bean, BigDecimal initialPrice) throws ApplicationException {
        PlantSpecies species = mapAndValidate(bean);
        validateInitialPrice(initialPrice);
        try {
            return plantSpeciesDao.createWithInitialPrice(species, initialPrice);
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public List<PlantSpeciesBean> getAllSpecies() throws ApplicationException {
        try {
            return plantSpeciesDao.findAll().stream().map(plantSpeciesMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve plant species", e);
        }
    }

    public Optional<PlantSpeciesBean> getSpeciesByCode(String speciesCode) throws ApplicationException {
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        try {
            return plantSpeciesDao.findByCode(normalizedCode).map(plantSpeciesMapper::toBean);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve plant species", e);
        }
    }

    public void updateSpecies(PlantSpeciesBean bean) throws ApplicationException {
        if (bean == null) throw new ApplicationException("Plant species cannot be null");
        String speciesCode = normalizeSpeciesCode(bean.getSpeciesCode());
        PlantSpecies existingSpecies = findSpeciesEntity(speciesCode);
        PlantSpecies species = mapAndValidate(bean, existingSpecies.getPlantType());
        try {
            plantSpeciesDao.update(species);
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public boolean deleteSpecies(String speciesCode) throws ApplicationException {
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        try {
            return plantSpeciesDao.deleteByCode(normalizedCode);
        } catch (DaoException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public boolean existsByCode(String speciesCode) throws ApplicationException {
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        try {
            return plantSpeciesDao.existsByCode(normalizedCode);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify plant species", e);
        }
    }

    public List<SpeciesCatalogItemBean> getCatalog() throws ApplicationException {
        try {
            return plantSpeciesDao.findCatalog().stream().map(catalogItemMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve plant species catalog", e);
        }
    }

    public List<SpeciesSalesReportBean> getSalesReport() throws ApplicationException {
        try {
            return plantSpeciesDao.findSalesReport().stream().map(salesReportMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve plant species sales report", e);
        }
    }

    public List<PlantSpeciesBean> searchByName(String searchTerm) throws ApplicationException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) throw new ApplicationException("Search term cannot be empty");
        try {
            return plantSpeciesDao.findByName(searchTerm.trim().toLowerCase()).stream().map(plantSpeciesMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to access plant species data", e);
        }
    }

    public List<PlantSpeciesBean> searchByPlantType(String plantType) throws ApplicationException {
        PlantType type = parsePlantType(plantType);
        try {
            return plantSpeciesDao.findByPlantType(type).stream().map(plantSpeciesMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to access plant species data", e);
        }
    }

    public List<PlantSpeciesBean> searchByFlowerColor(String flowerColor) throws ApplicationException {
        FlowerColor color = parseFlowerColor(flowerColor);
        try {
            return plantSpeciesDao.findByFlowerColor(color).stream().map(plantSpeciesMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to access plant species data", e);
        }
    }

    private PlantSpecies mapAndValidate(PlantSpeciesBean bean) throws ApplicationException {
        if (bean == null) throw new ApplicationException("Plant species cannot be null");
        try {
            return validateMappedSpecies(plantSpeciesMapper.toEntity(bean));
        } catch (ApplicationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    private PlantSpecies mapAndValidate(PlantSpeciesBean bean, PlantType plantType) throws ApplicationException {
        try {
            return validateMappedSpecies(plantSpeciesMapper.toEntity(bean, plantType));
        } catch (ApplicationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    private PlantSpecies validateMappedSpecies(PlantSpecies species) throws ApplicationException {
        if (isBlank(species.getCommonName())) throw new ApplicationException("Common name is required");
        if (isBlank(species.getLatinName())) throw new ApplicationException("Latin name is required");
        return species;
    }

    private PlantSpecies findSpeciesEntity(String speciesCode) throws ApplicationException {
        try {
            return plantSpeciesDao.findByCode(speciesCode).orElseThrow(() -> new ApplicationException("Plant species not found: " + speciesCode));
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve plant species", e);
        }
    }

    private void validateInitialPrice(BigDecimal price) throws ApplicationException {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApplicationException("Initial price must be greater than zero");
        }
    }

    private String normalizeSpeciesCode(String speciesCode) throws ApplicationException {
        if (speciesCode == null || speciesCode.trim().isEmpty()) throw new ApplicationException("Species code is required");
        return speciesCode.trim().toUpperCase();
    }

    private PlantType parsePlantType(String plantType) throws ApplicationException {
        if (plantType == null || plantType.trim().isEmpty()) throw new ApplicationException("Plant type is required");
        try {
            return PlantType.valueOf(plantType.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApplicationException("Invalid plant type", e);
        }
    }

    private FlowerColor parseFlowerColor(String flowerColor) throws ApplicationException {
        if (flowerColor == null || flowerColor.trim().isEmpty()) throw new ApplicationException("Flower color is required");
        try {
            return FlowerColor.valueOf(flowerColor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApplicationException("Invalid flower color", e);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
