package it.verde.persistence.jdbc;

import it.verde.exception.DaoException;
import it.verde.model.entity.PriceHistory;
import it.verde.persistence.connection.ConnectionProvider;
import it.verde.persistence.dao.PriceHistoryDao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public final class JdbcPriceHistoryDao extends AbstractJdbcDao implements PriceHistoryDao {
    private static final String INSERT_PRICE_VARIATION_PROCEDURE = "{CALL InserisciVariazionePrezzo(?, ?)}";
    private static final String FIND_PRICE_HISTORY_PROCEDURE = "{CALL VisualizzaStoricoPrezzi(?)}";
    private static final String FIND_CURRENT_PRICE_PROCEDURE = "{CALL OttieniPrezzoAttuale(?)}";

    public JdbcPriceHistoryDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public void insertPriceVariation(String speciesCode, BigDecimal newPrice) throws DaoException {
        String normalizedCode = requireSpeciesCode(speciesCode);
        if (newPrice == null || newPrice.signum() <= 0) throw new DaoException("Price must be greater than zero");

        try (CallableStatement statement = connection().prepareCall(INSERT_PRICE_VARIATION_PROCEDURE)) {
            statement.setString(1, normalizedCode);
            statement.setBigDecimal(2, newPrice);
            statement.execute();
        } catch (SQLException e) {
            throw procedureException("Unable to insert price variation", e);
        }
    }

    @Override
    public List<PriceHistory> findBySpeciesCode(String speciesCode) throws DaoException {
        String normalizedCode = requireSpeciesCode(speciesCode);
        List<PriceHistory> history = new ArrayList<>();

        try (CallableStatement statement = connection().prepareCall(FIND_PRICE_HISTORY_PROCEDURE)) {
            statement.setString(1, normalizedCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Timestamp changedAt = resultSet.getTimestamp("DataVariazione");
                    history.add(new PriceHistory(
                        changedAt.toLocalDateTime(),
                        resultSet.getBigDecimal("PrezzoPrecedente"),
                        resultSet.getBigDecimal("PrezzoAttuale"),
                        normalizedCode
                    ));
                }
            }
            return history;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve price history", e);
        }
    }

    @Override
    public BigDecimal findCurrentPrice(String speciesCode) throws DaoException {
        String normalizedCode = requireSpeciesCode(speciesCode);

        try (CallableStatement statement = connection().prepareCall(FIND_CURRENT_PRICE_PROCEDURE)) {
            statement.setString(1, normalizedCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) throw new DaoException("Current price not found for species: " + normalizedCode);

                BigDecimal price = resultSet.getBigDecimal("PrezzoAttuale");
                if (price == null) throw new DaoException("Current price not found for species: " + normalizedCode);

                return price;
            }
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve current price", e);
        }
    }

    private String requireSpeciesCode(String speciesCode) throws DaoException {
        if (speciesCode == null || speciesCode.isBlank()) throw new DaoException("Species code is required");
        return speciesCode.trim().toUpperCase();
    }
}
