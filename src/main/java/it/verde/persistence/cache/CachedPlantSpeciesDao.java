package it.verde.persistence.cache;

import it.verde.exception.DaoException;
import it.verde.model.entity.FloweringPlantSpecies;
import it.verde.model.entity.NonFloweringPlantSpecies;
import it.verde.model.entity.PlantSpecies;
import it.verde.model.readmodel.SpeciesCatalogEntry;
import it.verde.model.readmodel.SpeciesSalesReport;
import it.verde.model.type.FlowerColor;
import it.verde.model.type.PlantType;
import it.verde.persistence.connection.ConnectionProvider;
import it.verde.persistence.dao.PlantSpeciesDao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class CachedPlantSpeciesDao implements PlantSpeciesDao {
    private final PlantSpeciesDao delegate;
    private final ConnectionProvider connectionProvider;
    private final Map<String, PlantSpecies> speciesByCode = new HashMap<>();
    private List<PlantSpecies> allSpecies;
    private Connection sessionConnection;

    public CachedPlantSpeciesDao(PlantSpeciesDao delegate, ConnectionProvider connectionProvider) {
        this.delegate = Objects.requireNonNull(delegate, "Plant species DAO delegate cannot be null");
        this.connectionProvider = Objects.requireNonNull(connectionProvider, "Connection provider cannot be null");
    }

    @Override
    public String createWithInitialPrice(PlantSpecies species, BigDecimal initialPrice) throws DaoException {
        try {
            return delegate.createWithInitialPrice(species, initialPrice);
        } finally {
            clearCache();
        }
    }

    @Override
    public Optional<PlantSpecies> findByCode(String speciesCode) throws DaoException {
        if (speciesCode == null || speciesCode.isBlank()) return delegate.findByCode(speciesCode);
        ensureCurrentSession();

        String normalizedCode = speciesCode.trim().toUpperCase();
        PlantSpecies cached = speciesByCode.get(normalizedCode);
        if (cached != null) return Optional.of(copySpecies(cached));

        Optional<PlantSpecies> loaded = delegate.findByCode(normalizedCode);
        if (loaded.isEmpty()) return Optional.empty();

        PlantSpecies snapshot = copySpecies(loaded.get());
        speciesByCode.put(normalizedCode, snapshot);
        return Optional.of(copySpecies(snapshot));
    }

    @Override
    public List<PlantSpecies> findAll() throws DaoException {
        ensureCurrentSession();
        if (allSpecies != null) return copySpeciesList(allSpecies);

        List<PlantSpecies> loaded = delegate.findAll();
        List<PlantSpecies> snapshots = copySpeciesList(loaded);
        allSpecies = snapshots;

        for (PlantSpecies species : snapshots) {
            if (species.getSpeciesCode() != null) speciesByCode.put(species.getSpeciesCode().trim().toUpperCase(), copySpecies(species));
        }

        return copySpeciesList(snapshots);
    }

    @Override
    public void update(PlantSpecies species) throws DaoException {
        try {
            delegate.update(species);
        } finally {
            invalidateSpecies(species == null ? null : species.getSpeciesCode());
        }
    }

    @Override
    public boolean deleteByCode(String speciesCode) throws DaoException {
        try {
            return delegate.deleteByCode(speciesCode);
        } finally {
            invalidateSpecies(speciesCode);
        }
    }

    @Override
    public boolean existsByCode(String speciesCode) throws DaoException {
        return delegate.existsByCode(speciesCode);
    }

    @Override
    public List<SpeciesCatalogEntry> findCatalog() throws DaoException {
        return delegate.findCatalog();
    }

    @Override
    public List<SpeciesSalesReport> findSalesReport() throws DaoException {
        return delegate.findSalesReport();
    }

    @Override
    public List<PlantSpecies> findByName(String searchTerm) throws DaoException {
        return delegate.findByName(searchTerm);
    }

    @Override
    public List<PlantSpecies> findByPlantType(PlantType plantType) throws DaoException {
        return delegate.findByPlantType(plantType);
    }

    @Override
    public List<PlantSpecies> findByFlowerColor(FlowerColor flowerColor) throws DaoException {
        return delegate.findByFlowerColor(flowerColor);
    }

    public void clearCache() {
        speciesByCode.clear();
        allSpecies = null;
    }

    private void ensureCurrentSession() throws DaoException {
        Connection currentConnection = connectionProvider.getConnection();
        if (currentConnection == sessionConnection) return;
        clearCache();
        sessionConnection = currentConnection;
    }

    private void invalidateSpecies(String speciesCode) {
        if (speciesCode == null || speciesCode.isBlank()) speciesByCode.clear();
        else speciesByCode.remove(speciesCode.trim().toUpperCase());
        allSpecies = null;
    }

    private List<PlantSpecies> copySpeciesList(List<PlantSpecies> speciesList) {
        List<PlantSpecies> copies = new ArrayList<>(speciesList.size());
        for (PlantSpecies species : speciesList) copies.add(copySpecies(species));
        return copies;
    }

    private PlantSpecies copySpecies(PlantSpecies species) {
        if (species instanceof FloweringPlantSpecies floweringSpecies) {
            return new FloweringPlantSpecies(
                    floweringSpecies.getSpeciesCode(),
                    floweringSpecies.getLatinName(),
                    floweringSpecies.getCommonName(),
                    floweringSpecies.getPlantType(),
                    floweringSpecies.isExotic(),
                    floweringSpecies.getFlowerColor()
            );
        }

        return new NonFloweringPlantSpecies(
                species.getSpeciesCode(),
                species.getLatinName(),
                species.getCommonName(),
                species.getPlantType(),
                species.isExotic()
        );
    }
}
