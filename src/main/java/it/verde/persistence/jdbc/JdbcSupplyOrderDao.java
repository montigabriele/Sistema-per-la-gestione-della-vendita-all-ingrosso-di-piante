package it.verde.persistence.jdbc;

import it.verde.exception.DaoException;
import it.verde.model.entity.SupplyOrder;
import it.verde.model.entity.SupplyOrderItem;
import it.verde.model.type.OrderStatus;
import it.verde.persistence.connection.ConnectionProvider;
import it.verde.persistence.dao.SupplyOrderDao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public final class JdbcSupplyOrderDao extends AbstractJdbcDao implements SupplyOrderDao {
    private static final String CREATE_ORDER_PROCEDURE = "{CALL InserisciOrdineRifornimentoCompleto(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    private static final String FIND_ORDER_PROCEDURE = "{CALL RicercaOrdineRifornimento(?)}";
    private static final String FIND_ALL_ORDERS_PROCEDURE = "{CALL VisualizzaOrdiniRifornimento()}";
    private static final String FIND_ORDER_ITEMS_PROCEDURE = "{CALL VisualizzaDettagliOrdineRifornimento(?)}";
    private static final String UPDATE_ORDER_PROCEDURE = "{CALL ModificaOrdineRifornimentoAperto(?, ?, ?, ?, ?, ?, ?)}";
    private static final String DELETE_ORDER_PROCEDURE = "{CALL EliminaOrdineRifornimento(?)}";
    private static final String EXISTS_ORDER_PROCEDURE = "{CALL VerificaOrdineRifornimentoEsistente(?)}";
    private static final String UPDATE_STATUS_PROCEDURE = "{CALL ModificaStatoOrdineRifornimento(?, ?)}";
    private static final String FIND_BY_STATUS_PROCEDURE = "{CALL RicercaOrdiniRifornimentoPerStato(?)}";
    private static final String FIND_BY_SUPPLIER_PROCEDURE = "{CALL RicercaOrdiniRifornimentoPerFornitore(?)}";

    public JdbcSupplyOrderDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public Integer create(SupplyOrder order) throws DaoException {
        validateOrderForPersistence(order);
        if (order.getItems().isEmpty()) throw new DaoException("A supply order must contain at least one item");
        if (order.getStatus() != OrderStatus.OPEN) throw new DaoException("A new supply order must have OPEN status");

        try (CallableStatement statement = connection().prepareCall(CREATE_ORDER_PROCEDURE)) {
            statement.setInt(1, order.getSupplierId());
            statement.setDate(2, Date.valueOf(order.getOrderDate()));
            statement.setString(3, order.getContactEmail());
            statement.setString(4, order.getDeliveryStreet());
            statement.setString(5, order.getDeliveryCity());
            statement.setString(6, order.getDeliveryPostalCode());
            statement.setString(7, order.getContactPerson());
            statement.setString(8, order.getCourierContact());
            statement.setString(9, serializeItems(order.getItems()));
            statement.registerOutParameter(10, Types.INTEGER);
            statement.execute();

            int orderId = statement.getInt(10);
            order.setOrderId(orderId);
            return orderId;
        } catch (SQLException e) {
            throw dataAccessException("Unable to create supply order", e);
        }
    }

    @Override
    public Optional<SupplyOrder> findById(int orderId) throws DaoException {
        requireOrderId(orderId);

        try (CallableStatement statement = connection().prepareCall(FIND_ORDER_PROCEDURE)) {
            statement.setInt(1, orderId);
            boolean hasResults = statement.execute();
            if (!hasResults) return Optional.empty();

            SupplyOrder order;
            try (ResultSet resultSet = statement.getResultSet()) {
                if (!resultSet.next()) return Optional.empty();
                order = mapOrder(resultSet);
            }

            List<SupplyOrderItem> items = new ArrayList<>();
            if (statement.getMoreResults()) {
                try (ResultSet resultSet = statement.getResultSet()) {
                    while (resultSet.next()) items.add(mapItem(resultSet));
                }
            }
            order.setItems(items);
            return Optional.of(order);
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve supply order", e);
        }
    }

    @Override
    public List<SupplyOrder> findAll() throws DaoException {
        Connection connection = connection();
        List<SupplyOrder> orders = new ArrayList<>();

        try (CallableStatement statement = connection.prepareCall(FIND_ALL_ORDERS_PROCEDURE); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) orders.add(mapOrder(resultSet));
            for (SupplyOrder order : orders) order.setItems(findItems(connection, order.getOrderId()));
            return orders;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve supply orders", e);
        }
    }

    @Override
    public void update(SupplyOrder order) throws DaoException {
        validateOrderForPersistence(order);
        requireOrderId(order.getOrderId());

        try (CallableStatement statement = connection().prepareCall(UPDATE_ORDER_PROCEDURE)) {
            statement.setInt(1, order.getOrderId());
            statement.setString(2, order.getContactEmail());
            statement.setString(3, order.getDeliveryStreet());
            statement.setString(4, order.getDeliveryCity());
            statement.setString(5, order.getDeliveryPostalCode());
            statement.setString(6, order.getContactPerson());
            statement.setString(7, order.getCourierContact());
            statement.execute();
        } catch (SQLException e) {
            throw dataAccessException("Unable to update supply order", e);
        }
    }

    @Override
    public boolean deleteById(int orderId) throws DaoException {
        requireOrderId(orderId);

        try (CallableStatement statement = connection().prepareCall(DELETE_ORDER_PROCEDURE)) {
            statement.setInt(1, orderId);
            statement.execute();
        } catch (SQLException e) {
            throw dataAccessException("Unable to delete supply order", e);
        }

        return !existsById(orderId);
    }

    @Override
    public boolean existsById(int orderId) throws DaoException {
        if (orderId <= 0) return false;

        try (CallableStatement statement = connection().prepareCall(EXISTS_ORDER_PROCEDURE)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean("Esiste");
            }
        } catch (SQLException e) {
            throw dataAccessException("Unable to verify supply order", e);
        }
    }

    @Override
    public boolean updateStatus(int orderId, OrderStatus status) throws DaoException {
        requireOrderId(orderId);
        if (status == null) throw new DaoException("Order status is required");

        try (CallableStatement statement = connection().prepareCall(UPDATE_STATUS_PROCEDURE)) {
            statement.setInt(1, orderId);
            statement.setString(2, toDbStatus(status));
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw dataAccessException("Unable to update supply order status", e);
        }
    }

    @Override
    public List<SupplyOrder> findByStatus(OrderStatus status) throws DaoException {
        if (status == null) throw new DaoException("Order status is required");
        return findOrdersByStatus(toDbStatus(status));
    }

    @Override
    public List<SupplyOrder> findBySupplierId(int supplierId) throws DaoException {
        if (supplierId <= 0) throw new DaoException("Invalid supplier id");
        return findOrdersBySupplier(supplierId);
    }

    private List<SupplyOrder> findOrdersByStatus(String status) throws DaoException {
        Connection connection = connection();
        List<SupplyOrder> orders = new ArrayList<>();

        try (CallableStatement statement = connection.prepareCall(FIND_BY_STATUS_PROCEDURE)) {
            statement.setString(1, status);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) orders.add(mapOrder(resultSet));
            }
            for (SupplyOrder order : orders) order.setItems(findItems(connection, order.getOrderId()));
            return orders;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve supply orders by status", e);
        }
    }

    private List<SupplyOrder> findOrdersBySupplier(int supplierId) throws DaoException {
        Connection connection = connection();
        List<SupplyOrder> orders = new ArrayList<>();

        try (CallableStatement statement = connection.prepareCall(FIND_BY_SUPPLIER_PROCEDURE)) {
            statement.setInt(1, supplierId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) orders.add(mapOrder(resultSet));
            }
            for (SupplyOrder order : orders) order.setItems(findItems(connection, order.getOrderId()));
            return orders;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve supply orders by supplier", e);
        }
    }

    private List<SupplyOrderItem> findItems(Connection connection, int orderId) throws SQLException, DaoException {
        List<SupplyOrderItem> items = new ArrayList<>();

        try (CallableStatement statement = connection.prepareCall(FIND_ORDER_ITEMS_PROCEDURE)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) items.add(mapItem(resultSet));
            }
        }

        return items;
    }

    private SupplyOrderItem mapItem(ResultSet resultSet) throws SQLException, DaoException {
        try {
            return new SupplyOrderItem(resultSet.getString("CodiceSpecie"), resultSet.getInt("Quantita"));
        } catch (RuntimeException e) {
            throw new DaoException("Invalid supply order item data returned by the database", e);
        }
    }

    private SupplyOrder mapOrder(ResultSet resultSet) throws SQLException, DaoException {
        try {
            SupplyOrder order = new SupplyOrder(
                resultSet.getInt("IdOrdine"),
                resultSet.getDate("DataOrdine").toLocalDate(),
                fromDbStatus(resultSet.getString("Stato")),
                resultSet.getInt("CodiceFornitore")
            );
            order.setContactEmail(resultSet.getString("EmailContatto"));
            order.setDeliveryStreet(resultSet.getString("ViaConsegna"));
            order.setDeliveryCity(resultSet.getString("CittaConsegna"));
            order.setDeliveryPostalCode(resultSet.getString("CapConsegna"));
            order.setContactPerson(resultSet.getString("Referente"));
            order.setCourierContact(normalizePersistedPhone(resultSet.getString("RecapitoCorriere")));
            return order;
        } catch (RuntimeException e) {
            throw new DaoException("Invalid supply order data returned by the database", e);
        }
    }

    private String serializeItems(List<SupplyOrderItem> items) throws DaoException {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (SupplyOrderItem item : items) {
            if (item == null) throw new DaoException("Supply order item is required");
            joiner.add("{\"speciesCode\":\"" + escapeJson(item.getSpeciesCode()) + "\",\"quantity\":" + item.getQuantity() + "}");
        }
        return joiner.toString();
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String toDbStatus(OrderStatus status) throws DaoException {
        if (status == null) throw new DaoException("Order status is required");
        return switch (status) {
            case OPEN -> "APERTO";
            case CONFIRMED -> "CONFERMATO";
            case SHIPPED -> "SPEDITO";
            case DELIVERED -> "CONSEGNATO";
            case CANCELLED -> "ANNULLATO";
        };
    }

    private OrderStatus fromDbStatus(String status) throws DaoException {
        if (status == null) throw new DaoException("Database returned a null supply order status");
        return switch (status) {
            case "APERTO" -> OrderStatus.OPEN;
            case "CONFERMATO" -> OrderStatus.CONFIRMED;
            case "SPEDITO" -> OrderStatus.SHIPPED;
            case "CONSEGNATO" -> OrderStatus.DELIVERED;
            case "ANNULLATO" -> OrderStatus.CANCELLED;
            default -> throw new DaoException("Unsupported database supply order status: " + status);
        };
    }

    private String normalizePersistedPhone(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        boolean international = trimmed.startsWith("+");
        String digits = trimmed.replaceAll("\\D", "");
        return international ? "+" + digits : digits;
    }

    private void validateOrderForPersistence(SupplyOrder order) throws DaoException {
        if (order == null) throw new DaoException("Supply order is required");
        if (order.getSupplierId() <= 0) throw new DaoException("Invalid supplier id");
        if (order.getOrderDate() == null) throw new DaoException("Order date is required");
        if (order.getStatus() == null) throw new DaoException("Order status is required");
        requireNonBlank(order.getContactEmail(), "Contact email is required");
        requireNonBlank(order.getDeliveryStreet(), "Delivery street is required");
        requireNonBlank(order.getDeliveryCity(), "Delivery city is required");
        requireNonBlank(order.getDeliveryPostalCode(), "Delivery postal code is required");
        requireNonBlank(order.getContactPerson(), "Contact person is required");
        requireNonBlank(order.getCourierContact(), "Courier contact is required");
    }

    private void requireNonBlank(String value, String message) throws DaoException {
        if (value == null || value.isBlank()) throw new DaoException(message);
    }

    private void requireOrderId(int orderId) throws DaoException {
        if (orderId <= 0) throw new DaoException("Invalid supply order id");
    }
}
