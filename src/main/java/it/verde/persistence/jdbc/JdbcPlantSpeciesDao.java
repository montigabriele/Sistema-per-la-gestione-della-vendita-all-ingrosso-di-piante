package it.verde.persistence.jdbc;

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
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class JdbcPlantSpeciesDao extends AbstractJdbcDao implements PlantSpeciesDao {
    private static final String COLUMN_SPECIES_CODE = "CodiceSpecie";
    private static final String COLUMN_COMMON_NAME = "NomeComune";
    private static final String COLUMN_PLANT_TYPE = "Tipologia";
    private static final String COLUMN_FLOWERING = "Fiorita";
    private static final String INSERT_SPECIES_WITH_PRICE_PROCEDURE = "{CALL InserisciSpecieConPrezzo(?, ?, ?, ?, ?, ?, ?, ?)}";
    private static final String FIND_SPECIES_PROCEDURE = "{CALL VisualizzaSpecie(?)}";
    private static final String FIND_ALL_SPECIES_PROCEDURE = "{CALL VisualizzaSpecieTotali()}";
    private static final String UPDATE_SPECIES_PROCEDURE = "{CALL ModificaSpecie(?, ?, ?, ?, ?, ?, ?)}";
    private static final String DELETE_SPECIES_PROCEDURE = "{CALL EliminaSpecie(?)}";
    private static final String FIND_BY_NAME_PROCEDURE = "{CALL RicercaSpeciePerNome(?)}";
    private static final String FIND_BY_PLANT_TYPE_PROCEDURE = "{CALL RicercaSpeciePerTipologia(?)}";
    private static final String FIND_BY_FLOWER_COLOR_PROCEDURE = "{CALL RicercaSpeciePerColorazione(?)}";
    private static final String EXISTS_BY_CODE_PROCEDURE = "{CALL VerificaSpecieEsistente(?)}";
    private static final String FIND_CATALOG_PROCEDURE = "{CALL VisualizzaCatalogoPiante()}";
    private static final String FIND_SALES_REPORT_PROCEDURE = "{CALL VisualizzaReportVenditeSpecie()}";

    public JdbcPlantSpeciesDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public String createWithInitialPrice(PlantSpecies species, BigDecimal initialPrice) throws DaoException {
        validateSpeciesData(species);
        if (initialPrice == null || initialPrice.signum() <= 0) {
            throw new DaoException("Initial price must be greater than zero");
        }

        try (CallableStatement statement = connection().prepareCall(INSERT_SPECIES_WITH_PRICE_PROCEDURE)) {
            statement.setString(1, species.getCommonName().trim());
            statement.setString(2, species.getLatinName().trim());
            statement.setString(3, toDbPlantType(species.getPlantType()));
            statement.setBoolean(4, species.isExotic());
            statement.setBoolean(5, species.isFlowering());
            if (species instanceof FloweringPlantSpecies floweringSpecies) {
                statement.setString(6, toDbFlowerColor(floweringSpecies.getFlowerColor()));
            } else {
                statement.setNull(6, Types.VARCHAR);
            }
            statement.setBigDecimal(7, initialPrice);
            statement.registerOutParameter(8, Types.CHAR);
            statement.execute();

            String code = statement.getString(8);
            if (code == null || code.isBlank()) {
                throw new DaoException("Database did not generate a species code");
            }
            return code.trim().toUpperCase();
        } catch (SQLException e) {
            throw procedureException("Unable to create plant species", e);
        }
    }

    @Override
    public Optional<PlantSpecies> findByCode(String speciesCode) throws DaoException {
        if (speciesCode == null || speciesCode.isBlank()) return Optional.empty();

        try (CallableStatement statement = connection().prepareCall(FIND_SPECIES_PROCEDURE)) {
            statement.setString(1, speciesCode.trim().toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) return Optional.empty();
                return Optional.of(mapSpecies(resultSet));
            }
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve plant species", e);
        }
    }

    @Override
    public List<PlantSpecies> findAll() throws DaoException {
        List<PlantSpecies> species = new ArrayList<>();

        try (CallableStatement statement = connection().prepareCall(FIND_ALL_SPECIES_PROCEDURE); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) species.add(mapSpecies(resultSet));
            return species;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve plant species", e);
        }
    }

    @Override
    public void update(PlantSpecies species) throws DaoException {
        validateSpeciesData(species);
        executeWriteProcedure(UPDATE_SPECIES_PROCEDURE, species, "Unable to update plant species");
    }

    @Override
    public boolean deleteByCode(String speciesCode) throws DaoException {
        String normalizedCode = requireSpeciesCode(speciesCode);

        try (CallableStatement statement = connection().prepareCall(DELETE_SPECIES_PROCEDURE)) {
            statement.setString(1, normalizedCode);
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw procedureException("Unable to delete plant species", e);
        }
    }

    @Override
    public boolean existsByCode(String speciesCode) throws DaoException {
        if (speciesCode == null || speciesCode.isBlank()) return false;

        try (CallableStatement statement = connection().prepareCall(EXISTS_BY_CODE_PROCEDURE)) {
            statement.setString(1, speciesCode.trim().toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean("Esiste");
            }
        } catch (SQLException e) {
            throw dataAccessException("Unable to verify plant species", e);
        }
    }

    @Override
    public List<SpeciesCatalogEntry> findCatalog() throws DaoException {
        List<SpeciesCatalogEntry> catalog = new ArrayList<>();

        try (CallableStatement statement = connection().prepareCall(FIND_CATALOG_PROCEDURE); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) catalog.add(mapCatalogEntry(resultSet));
            return catalog;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve plant species catalog", e);
        }
    }

    @Override
    public List<SpeciesSalesReport> findSalesReport() throws DaoException {
        List<SpeciesSalesReport> report = new ArrayList<>();

        try (CallableStatement statement = connection().prepareCall(FIND_SALES_REPORT_PROCEDURE); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) report.add(mapSalesReport(resultSet));
            return report;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve plant species sales report", e);
        }
    }

    @Override
    public List<PlantSpecies> findByName(String searchTerm) throws DaoException {
        if (searchTerm == null || searchTerm.isBlank()) return List.of();
        return executeSpeciesSearch(FIND_BY_NAME_PROCEDURE, searchTerm.trim(), "Unable to search plant species by name");
    }

    @Override
    public List<PlantSpecies> findByPlantType(PlantType plantType) throws DaoException {
        if (plantType == null) throw new DaoException("Plant type is required");
        return executeSpeciesSearch(FIND_BY_PLANT_TYPE_PROCEDURE, toDbPlantType(plantType), "Unable to search plant species by type");
    }

    @Override
    public List<PlantSpecies> findByFlowerColor(FlowerColor flowerColor) throws DaoException {
        if (flowerColor == null) throw new DaoException("Flower color is required");
        return executeSpeciesSearch(FIND_BY_FLOWER_COLOR_PROCEDURE, toDbFlowerColor(flowerColor), "Unable to search plant species by flower color");
    }

    private void executeWriteProcedure(String procedure, PlantSpecies species, String errorMessage) throws DaoException {
        try (CallableStatement statement = connection().prepareCall(procedure)) {
            statement.setString(1, requireSpeciesCode(species.getSpeciesCode()));
            statement.setString(2, species.getCommonName().trim());
            statement.setString(3, species.getLatinName().trim());
            statement.setString(4, toDbPlantType(species.getPlantType()));
            statement.setBoolean(5, species.isExotic());
            statement.setBoolean(6, species.isFlowering());
            if (species instanceof FloweringPlantSpecies floweringSpecies) statement.setString(7, toDbFlowerColor(floweringSpecies.getFlowerColor()));
            else statement.setNull(7, Types.VARCHAR);
            statement.execute();
        } catch (SQLException e) {
            throw procedureException(errorMessage, e);
        }
    }

    private List<PlantSpecies> executeSpeciesSearch(String procedure, String value, String errorMessage) throws DaoException {
        List<PlantSpecies> species = new ArrayList<>();

        try (CallableStatement statement = connection().prepareCall(procedure)) {
            statement.setString(1, value);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) species.add(mapSpecies(resultSet));
            }
            return species;
        } catch (SQLException e) {
            throw dataAccessException(errorMessage, e);
        }
    }

    private PlantSpecies mapSpecies(ResultSet resultSet) throws SQLException, DaoException {
        String speciesCode = resultSet.getString(COLUMN_SPECIES_CODE);
        String commonName = resultSet.getString(COLUMN_COMMON_NAME);
        String latinName = resultSet.getString("NomeLatino");
        PlantType plantType = fromDbPlantType(resultSet.getString(COLUMN_PLANT_TYPE));
        boolean exotic = resultSet.getBoolean("Esotica");
        boolean flowering = resultSet.getBoolean(COLUMN_FLOWERING);

        if (!flowering) return new NonFloweringPlantSpecies(speciesCode, latinName, commonName, plantType, exotic);
        return new FloweringPlantSpecies(speciesCode, latinName, commonName, plantType, exotic, fromDbFlowerColor(resultSet.getString("Colorazioni")));
    }

    private SpeciesCatalogEntry mapCatalogEntry(ResultSet resultSet) throws SQLException, DaoException {
        boolean flowering = resultSet.getBoolean(COLUMN_FLOWERING);
        FlowerColor flowerColor = flowering ? fromDbFlowerColor(resultSet.getString("Colorazioni")) : null;
        BigDecimal currentPrice = resultSet.getBigDecimal("PrezzoAttuale");
        if (currentPrice == null) currentPrice = BigDecimal.ZERO;
        return new SpeciesCatalogEntry(resultSet.getString(COLUMN_SPECIES_CODE), resultSet.getString(COLUMN_COMMON_NAME), resultSet.getString("NomeLatino"), fromDbPlantType(resultSet.getString(COLUMN_PLANT_TYPE)), resultSet.getBoolean("Esotica"), flowerColor, currentPrice, resultSet.getInt("Giacenza"));
    }

    private SpeciesSalesReport mapSalesReport(ResultSet resultSet) throws SQLException, DaoException {
        BigDecimal totalSalesValue = resultSet.getBigDecimal("ValoreTotaleVendite");
        if (totalSalesValue == null) totalSalesValue = BigDecimal.ZERO;
        return new SpeciesSalesReport(resultSet.getString(COLUMN_SPECIES_CODE), resultSet.getString(COLUMN_COMMON_NAME), fromDbPlantType(resultSet.getString(COLUMN_PLANT_TYPE)), resultSet.getBoolean(COLUMN_FLOWERING), resultSet.getInt("QuantitaVenduta"), totalSalesValue);
    }

    private String toDbPlantType(PlantType plantType) {
        return switch (plantType) {
            case INDOOR -> "APPARTAMENTO";
            case GARDEN -> "GIARDINO";
        };
    }

    private PlantType fromDbPlantType(String value) throws DaoException {
        if (value == null) throw new DaoException("Database returned a null plant type");
        return switch (value.trim().toUpperCase()) {
            case "APPARTAMENTO" -> PlantType.INDOOR;
            case "GIARDINO" -> PlantType.GARDEN;
            default -> throw new DaoException("Unsupported database plant type: " + value);
        };
    }

    private String toDbFlowerColor(FlowerColor flowerColor) {
        return switch (flowerColor) {
            case RED -> "ROSSA";
            case YELLOW -> "GIALLA";
            case ORANGE -> "ARANCIONE";
            case BLUE -> "BLU";
            case PURPLE -> "VIOLA";
            case WHITE -> "BIANCA";
            case MULTICOLOR -> "MULTICOLORE";
        };
    }

    private FlowerColor fromDbFlowerColor(String value) throws DaoException {
        if (value == null || value.isBlank()) throw new DaoException("Database returned an invalid flower color");
        return switch (value.trim().toUpperCase()) {
            case "ROSSA" -> FlowerColor.RED;
            case "GIALLA" -> FlowerColor.YELLOW;
            case "ARANCIONE" -> FlowerColor.ORANGE;
            case "BLU" -> FlowerColor.BLUE;
            case "VIOLA" -> FlowerColor.PURPLE;
            case "BIANCA" -> FlowerColor.WHITE;
            case "MULTICOLORE" -> FlowerColor.MULTICOLOR;
            default -> throw new DaoException("Unsupported database flower color: " + value);
        };
    }

    private void validateSpeciesData(PlantSpecies species) throws DaoException {
        if (species == null) throw new DaoException("Plant species is required");
        if (species.getCommonName() == null || species.getCommonName().isBlank()) throw new DaoException("Common name is required");
        if (species.getLatinName() == null || species.getLatinName().isBlank()) throw new DaoException("Latin name is required");
        if (species.getPlantType() == null) throw new DaoException("Plant type is required");
        if (species.isFlowering() && (!(species instanceof FloweringPlantSpecies floweringSpecies) || floweringSpecies.getFlowerColor() == null)) throw new DaoException("Flower color is required for flowering species");
    }

    private String requireSpeciesCode(String speciesCode) throws DaoException {
        if (speciesCode == null || speciesCode.isBlank()) throw new DaoException("Species code is required");
        return speciesCode.trim().toUpperCase();
    }
}
