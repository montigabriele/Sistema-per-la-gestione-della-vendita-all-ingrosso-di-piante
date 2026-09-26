package it.verde.persistence.jdbc;

import it.verde.exception.DaoException;
import it.verde.model.entity.Address;
import it.verde.model.entity.FloweringPlantSpecies;
import it.verde.model.entity.NonFloweringPlantSpecies;
import it.verde.model.entity.PlantSpecies;
import it.verde.model.entity.Supplier;
import it.verde.model.type.AddressType;
import it.verde.model.type.FlowerColor;
import it.verde.model.type.PlantType;
import it.verde.persistence.connection.ConnectionProvider;
import it.verde.persistence.dao.SupplierDao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public final class JdbcSupplierDao extends AbstractJdbcDao implements SupplierDao {
    private static final String INSERT_SUPPLIER_PROCEDURE = "{CALL InserisciFornitoreCompleto(?, ?, ?, ?)}";
    private static final String UPDATE_SUPPLIER_PROCEDURE = "{CALL ModificaFornitoreCompleto(?, ?, ?, ?)}";
    private static final String DELETE_SUPPLIER_PROCEDURE = "{CALL EliminaFornitore(?)}";
    private static final String FIND_SUPPLIER_PROCEDURE = "{CALL RicercaFornitorePerCodice(?)}";
    private static final String FIND_ALL_SUPPLIERS_PROCEDURE = "{CALL VisualizzaFornitori()}";
    private static final String EXISTS_SUPPLIER_PROCEDURE = "{CALL VerificaFornitoreEsistente(?)}";
    private static final String FIND_ADDRESSES_PROCEDURE = "{CALL VisualizzaIndirizziFornitore(?)}";
    private static final String ADD_SUPPLIED_SPECIES_PROCEDURE = "{CALL InserisciFornituraSpecie(?, ?)}";
    private static final String REMOVE_SUPPLIED_SPECIES_PROCEDURE = "{CALL EliminaFornituraSpecie(?, ?)}";
    private static final String FIND_SUPPLIED_SPECIES_PROCEDURE = "{CALL SpecieForniteDaFornitore(?)}";
    private static final String SUPPLIES_SPECIES_PROCEDURE = "{CALL VerificaFornituraSpecie(?, ?)}";

    public JdbcSupplierDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public Integer create(Supplier supplier) throws DaoException {
        validateSupplier(supplier, false);
        try (CallableStatement statement = connection().prepareCall(INSERT_SUPPLIER_PROCEDURE)) {
            statement.setString(1, supplier.getName().trim());
            statement.setString(2, supplier.getTaxCode().trim().toUpperCase());
            statement.setString(3, serializeAddresses(supplier.getAddresses()));
            statement.registerOutParameter(4, Types.INTEGER);
            statement.execute();
            int supplierId = statement.getInt(4);
            supplier.setSupplierId(supplierId);
            return supplierId;
        } catch (SQLException e) {
            throw dataAccessException("Unable to create supplier", e);
        }
    }

    @Override
    public Optional<Supplier> findById(Integer supplierId) throws DaoException {
        if (supplierId == null || supplierId <= 0) return Optional.empty();
        Connection connection = connection();

        try (CallableStatement statement = connection.prepareCall(FIND_SUPPLIER_PROCEDURE)) {
            statement.setInt(1, supplierId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) return Optional.empty();
                Supplier supplier = mapSupplier(resultSet);
                supplier.setAddresses(findAddresses(connection, supplierId));
                return Optional.of(supplier);
            }
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve supplier", e);
        }
    }

    @Override
    public List<Supplier> findAll() throws DaoException {
        Connection connection = connection();
        List<Supplier> suppliers = new ArrayList<>();

        try (CallableStatement statement = connection.prepareCall(FIND_ALL_SUPPLIERS_PROCEDURE); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) suppliers.add(mapSupplier(resultSet));
            for (Supplier supplier : suppliers) supplier.setAddresses(findAddresses(connection, supplier.getSupplierId()));
            return suppliers;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve suppliers", e);
        }
    }

    @Override
    public void update(Supplier supplier) throws DaoException {
        validateSupplier(supplier, true);
        try (CallableStatement statement = connection().prepareCall(UPDATE_SUPPLIER_PROCEDURE)) {
            statement.setInt(1, supplier.getSupplierId());
            statement.setString(2, supplier.getName().trim());
            statement.setString(3, supplier.getTaxCode().trim().toUpperCase());
            statement.setString(4, serializeAddresses(supplier.getAddresses()));
            statement.execute();
        } catch (SQLException e) {
            throw dataAccessException("Unable to update supplier", e);
        }
    }

    @Override
    public boolean deleteById(Integer supplierId) throws DaoException {
        requireSupplierId(supplierId);

        try (CallableStatement statement = connection().prepareCall(DELETE_SUPPLIER_PROCEDURE)) {
            statement.setInt(1, supplierId);
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw dataAccessException("Unable to delete supplier", e);
        }
    }

    @Override
    public boolean existsById(Integer supplierId) throws DaoException {
        if (supplierId == null || supplierId <= 0) return false;

        try (CallableStatement statement = connection().prepareCall(EXISTS_SUPPLIER_PROCEDURE)) {
            statement.setInt(1, supplierId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean("Esiste");
            }
        } catch (SQLException e) {
            throw dataAccessException("Unable to verify supplier", e);
        }
    }

    @Override
    public void addSuppliedSpecies(Integer supplierId, String speciesCode) throws DaoException {
        requireSupplierId(supplierId);
        String normalizedCode = requireSpeciesCode(speciesCode);

        try (CallableStatement statement = connection().prepareCall(ADD_SUPPLIED_SPECIES_PROCEDURE)) {
            statement.setInt(1, supplierId);
            statement.setString(2, normalizedCode);
            statement.execute();
        } catch (SQLException e) {
            throw dataAccessException("Unable to add supplied species", e);
        }
    }

    @Override
    public void removeSuppliedSpecies(Integer supplierId, String speciesCode) throws DaoException {
        requireSupplierId(supplierId);
        String normalizedCode = requireSpeciesCode(speciesCode);

        try (CallableStatement statement = connection().prepareCall(REMOVE_SUPPLIED_SPECIES_PROCEDURE)) {
            statement.setInt(1, supplierId);
            statement.setString(2, normalizedCode);
            statement.execute();
        } catch (SQLException e) {
            throw dataAccessException("Unable to remove supplied species", e);
        }
    }

    @Override
    public List<String> findSuppliedSpeciesCodes(Integer supplierId) throws DaoException {
        requireSupplierId(supplierId);
        List<String> speciesCodes = new ArrayList<>();

        try (CallableStatement statement = connection().prepareCall(FIND_SUPPLIED_SPECIES_PROCEDURE)) {
            statement.setInt(1, supplierId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) speciesCodes.add(resultSet.getString("CodiceSpecie"));
            }
            return speciesCodes;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve supplied species codes", e);
        }
    }

    @Override
    public boolean suppliesSpecies(Integer supplierId, String speciesCode) throws DaoException {
        if (supplierId == null || supplierId <= 0 || speciesCode == null || speciesCode.isBlank()) return false;

        try (CallableStatement statement = connection().prepareCall(SUPPLIES_SPECIES_PROCEDURE)) {
            statement.setInt(1, supplierId);
            statement.setString(2, speciesCode.trim().toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean("Esiste");
            }
        } catch (SQLException e) {
            throw dataAccessException("Unable to verify supplied species", e);
        }
    }

    @Override
    public List<PlantSpecies> findSuppliedSpecies(Integer supplierId) throws DaoException {
        requireSupplierId(supplierId);
        List<PlantSpecies> species = new ArrayList<>();

        try (CallableStatement statement = connection().prepareCall(FIND_SUPPLIED_SPECIES_PROCEDURE)) {
            statement.setInt(1, supplierId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) species.add(mapSuppliedSpecies(resultSet));
            }
            return species;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve supplied species", e);
        }
    }

    private List<Address> findAddresses(Connection connection, Integer supplierId) throws SQLException, DaoException {
        List<Address> addresses = new ArrayList<>();

        try (CallableStatement statement = connection.prepareCall(FIND_ADDRESSES_PROCEDURE)) {
            statement.setInt(1, supplierId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    addresses.add(new Address(resultSet.getInt("IdIndirizzo"), fromDbAddressType(resultSet.getString("Tipo")), resultSet.getString("Via"), resultSet.getString("CAP"), resultSet.getString("Citta")));
                }
            }
        }

        return addresses;
    }

    private Supplier mapSupplier(ResultSet resultSet) throws SQLException {
        return new Supplier(resultSet.getInt("CodiceFornitore"), resultSet.getString("Nome"), resultSet.getString("CodiceFiscale"));
    }

    private PlantSpecies mapSuppliedSpecies(ResultSet resultSet) throws SQLException, DaoException {
        String speciesCode = resultSet.getString("CodiceSpecie");
        String latinName = resultSet.getString("NomeLatino");
        String commonName = resultSet.getString("NomeComune");
        PlantType plantType = fromDbPlantType(resultSet.getString("Tipologia"));
        boolean exotic = resultSet.getBoolean("Esotica");
        String colorValue = resultSet.getString("Colorazione");

        if (colorValue == null || colorValue.isBlank()) return new NonFloweringPlantSpecies(speciesCode, latinName, commonName, plantType, exotic);
        return new FloweringPlantSpecies(speciesCode, latinName, commonName, plantType, exotic, fromDbFlowerColor(colorValue));
    }

    private String serializeAddresses(List<Address> addresses) throws DaoException {
        if (addresses == null || addresses.isEmpty()) throw new DaoException("A supplier must have at least one address");

        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (Address address : addresses) {
            validateAddress(address);
            joiner.add("{\"type\":\"" + toDbAddressType(address.getType()) + "\",\"street\":\"" + escapeJson(address.getStreet().trim()) + "\",\"postalCode\":\"" + escapeJson(address.getPostalCode().trim()) + "\",\"city\":\"" + escapeJson(address.getCity().trim()) + "\"}");
        }
        return joiner.toString();
    }

    private void validateAddress(Address address) throws DaoException {
        if (address == null) throw new DaoException("Supplier address is required");
        if (address.getType() == null) throw new DaoException("Address type is required");
        if (address.getStreet() == null || address.getStreet().isBlank()) throw new DaoException("Address street is required");
        if (address.getPostalCode() == null || !address.getPostalCode().trim().matches("\\d{5}")) throw new DaoException("Address postal code must contain 5 digits");
        if (address.getCity() == null || address.getCity().isBlank()) throw new DaoException("Address city is required");
    }

    private String escapeJson(String value) {
        StringBuilder escaped = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '\\' -> escaped.append("\\\\");
                case '\"' -> escaped.append("\\\"");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> {
                    if (ch < 0x20) escaped.append(String.format("\\u%04x", (int) ch));
                    else escaped.append(ch);
                }
            }
        }
        return escaped.toString();
    }

    private String toDbAddressType(AddressType type) throws DaoException {
        if (type == null) throw new DaoException("Address type is required");
        return switch (type) {
            case LEGAL -> "LEGALE";
            case BILLING -> "FATTURAZIONE";
        };
    }

    private AddressType fromDbAddressType(String value) throws DaoException {
        if (value == null) throw new DaoException("Database returned a null address type");
        return switch (value.trim().toUpperCase()) {
            case "LEGALE" -> AddressType.LEGAL;
            case "FATTURAZIONE" -> AddressType.BILLING;
            default -> throw new DaoException("Unsupported database address type: " + value);
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

    private FlowerColor fromDbFlowerColor(String value) throws DaoException {
        if (value == null) throw new DaoException("Database returned a null flower color");
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

    private void validateSupplier(Supplier supplier, boolean requireId) throws DaoException {
        if (supplier == null) throw new DaoException("Supplier is required");
        if (requireId) requireSupplierId(supplier.getSupplierId());
        if (supplier.getName() == null || supplier.getName().isBlank()) throw new DaoException("Supplier name is required");
        if (supplier.getTaxCode() == null || supplier.getTaxCode().isBlank()) throw new DaoException("Supplier tax code is required");
    }

    private void requireSupplierId(Integer supplierId) throws DaoException {
        if (supplierId == null || supplierId <= 0) throw new DaoException("Invalid supplier id");
    }

    private String requireSpeciesCode(String speciesCode) throws DaoException {
        if (speciesCode == null || speciesCode.isBlank()) throw new DaoException("Species code is required");
        return speciesCode.trim().toUpperCase();
    }
}
