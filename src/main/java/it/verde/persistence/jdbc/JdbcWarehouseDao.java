package it.verde.persistence.jdbc;

import it.verde.exception.DaoException;
import it.verde.model.entity.WarehouseStock;
import it.verde.model.readmodel.CriticalStock;
import it.verde.persistence.connection.ConnectionProvider;
import it.verde.persistence.dao.WarehouseDao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class JdbcWarehouseDao extends AbstractJdbcDao implements WarehouseDao {
    private static final String INSERT_STOCK_PROCEDURE = "{CALL InserisciGiacenza(?, ?)}";
    private static final String UPDATE_STOCK_PROCEDURE = "{CALL ModificaGiacenza(?, ?)}";
    private static final String FIND_QUANTITY_PROCEDURE = "{CALL OttieniQuantitaGiacenza(?)}";
    private static final String FIND_CRITICAL_STOCKS_PROCEDURE = "{CALL VisualizzaGiacenzeCritiche()}";

    public JdbcWarehouseDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public void create(WarehouseStock stock) throws DaoException {
        if (stock == null) throw new DaoException("Warehouse stock is required");

        try (CallableStatement statement = connection().prepareCall(INSERT_STOCK_PROCEDURE)) {
            statement.setString(1, stock.getSpeciesCode());
            statement.setInt(2, stock.getQuantity());
            statement.execute();
        } catch (SQLException e) {
            throw procedureException("Unable to create warehouse stock", e);
        }
    }
    
    @Override
    public void updateQuantity(String speciesCode, int quantity) throws DaoException {
        String normalizedCode = requireSpeciesCode(speciesCode);
        if (quantity < 0) throw new DaoException("Warehouse quantity cannot be negative");

        try (CallableStatement statement = connection().prepareCall(UPDATE_STOCK_PROCEDURE)) {
            statement.setString(1, normalizedCode);
            statement.setInt(2, quantity);
            statement.execute();
        } catch (SQLException e) {
            throw procedureException("Unable to update warehouse stock", e);
        }
    }

    @Override
    public int findQuantityBySpeciesCode(String speciesCode) throws DaoException {
        String normalizedCode = requireSpeciesCode(speciesCode);

        try (CallableStatement statement = connection().prepareCall(FIND_QUANTITY_PROCEDURE)) {
            statement.setString(1, normalizedCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt("Quantita") : 0;
            }
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve warehouse quantity", e);
        }
    }

    @Override
    public List<CriticalStock> findCriticalStocks() throws DaoException {
        List<CriticalStock> stocks = new ArrayList<>();

        try (CallableStatement statement = connection().prepareCall(FIND_CRITICAL_STOCKS_PROCEDURE); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                stocks.add(new CriticalStock(
                    resultSet.getString("CodiceSpecie"),
                    resultSet.getString("NomeComune"),
                    resultSet.getString("NomeLatino"),
                    resultSet.getInt("Quantita")
                ));
            }
            return stocks;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve critical warehouse stocks", e);
        }
    }

    private String requireSpeciesCode(String speciesCode) throws DaoException {
        if (speciesCode == null || speciesCode.isBlank()) throw new DaoException("Species code is required");
        return speciesCode.trim().toUpperCase();
    }
}
