package it.verde.persistence.jdbc;

import it.verde.exception.DaoException;
import it.verde.model.entity.SalesOrder;
import it.verde.model.entity.SalesOrderItem;
import it.verde.model.type.OrderStatus;
import it.verde.persistence.connection.ConnectionProvider;
import it.verde.persistence.dao.SalesOrderDao;

import java.math.BigDecimal;
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

public final class JdbcSalesOrderDao extends AbstractJdbcDao implements SalesOrderDao {
    private static final String CREATE_ORDER_PROCEDURE = "{CALL InserisciOrdineVenditaCompleto(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    private static final String FIND_ORDER_PROCEDURE = "{CALL RicercaOrdineVendita(?)}";
    private static final String FIND_ALL_ORDERS_PROCEDURE = "{CALL VisualizzaOrdiniVendita()}";
    private static final String FIND_ORDER_ITEMS_PROCEDURE = "{CALL VisualizzaDettagliOrdineVendita(?)}";
    private static final String UPDATE_ORDER_PROCEDURE = "{CALL ModificaOrdineVenditaAperto(?, ?, ?, ?, ?, ?)}";
    private static final String DELETE_ORDER_PROCEDURE = "{CALL EliminaOrdineVendita(?)}";
    private static final String EXISTS_ORDER_PROCEDURE = "{CALL VerificaOrdineVenditaEsistente(?)}";
    private static final String UPDATE_STATUS_PROCEDURE = "{CALL ModificaStatoOrdine(?, ?)}";
    private static final String FIND_BY_STATUS_PROCEDURE = "{CALL RicercaOrdiniPerStato(?)}";
    private static final String FIND_BY_CUSTOMER_PROCEDURE = "{CALL RicercaOrdiniPerAzienda(?)}";

    public JdbcSalesOrderDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public Integer create(SalesOrder order) throws DaoException {
        validateOrderForPersistence(order);
        if (order.getItems().isEmpty()) throw new DaoException("A sales order must contain at least one item");
        if (order.getStatus() != OrderStatus.OPEN) throw new DaoException("A new sales order must have OPEN status");

        try (CallableStatement statement = connection().prepareCall(CREATE_ORDER_PROCEDURE)) {
            statement.setString(1, order.getCustomerVatNumber());
            statement.setDate(2, Date.valueOf(order.getOrderDate()));
            statement.setString(3, order.getDeliveryStreet());
            statement.setString(4, order.getDeliveryCity());
            statement.setString(5, order.getDeliveryPostalCode());
            statement.setString(6, order.getContactPerson());
            statement.setString(7, order.getCourierContact());
            statement.setString(8, serializeItems(order.getItems()));
            statement.registerOutParameter(9, Types.INTEGER);
            statement.execute();

            int orderId = statement.getInt(9);
            order.setOrderId(orderId);
            return orderId;
        } catch (SQLException e) {
            throw procedureException("Unable to create sales order", e);
        }
    }

    @Override
    public Optional<SalesOrder> findById(int orderId) throws DaoException {
        requireOrderId(orderId);
        Connection connection = connection();

        try (CallableStatement statement = connection.prepareCall(FIND_ORDER_PROCEDURE)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) return Optional.empty();
                SalesOrder order = mapOrder(resultSet);
                order.setItems(findItems(connection, orderId));
                return Optional.of(order);
            }
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve sales order", e);
        }
    }

    @Override
    public List<SalesOrder> findAll() throws DaoException {
        Connection connection = connection();
        List<SalesOrder> orders = new ArrayList<>();

        try (CallableStatement statement = connection.prepareCall(FIND_ALL_ORDERS_PROCEDURE); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) orders.add(mapOrder(resultSet));
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve sales orders", e);
        }

        try {
            for (SalesOrder order : orders) order.setItems(findItems(connection, order.getOrderId()));
            return orders;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve sales order items", e);
        }
    }

    @Override
    public void update(SalesOrder order) throws DaoException {
        validateOrderForPersistence(order);
        requireOrderId(order.getOrderId());

        try (CallableStatement statement = connection().prepareCall(UPDATE_ORDER_PROCEDURE)) {
            statement.setInt(1, order.getOrderId());
            statement.setString(2, order.getDeliveryStreet());
            statement.setString(3, order.getDeliveryCity());
            statement.setString(4, order.getDeliveryPostalCode());
            statement.setString(5, order.getContactPerson());
            statement.setString(6, order.getCourierContact());
            statement.execute();
        } catch (SQLException e) {
            throw procedureException("Unable to update sales order", e);
        }
    }

    @Override
    public boolean deleteById(int orderId) throws DaoException {
        requireOrderId(orderId);

        try (CallableStatement statement = connection().prepareCall(DELETE_ORDER_PROCEDURE)) {
            statement.setInt(1, orderId);
            statement.execute();
        } catch (SQLException e) {
            throw procedureException("Unable to delete sales order", e);
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
            throw dataAccessException("Unable to verify sales order", e);
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
            throw procedureException("Unable to update sales order status", e);
        }
    }

    @Override
    public List<SalesOrder> findByStatus(OrderStatus status) throws DaoException {
        if (status == null) throw new DaoException("Order status is required");
        return findOrdersByProcedure(FIND_BY_STATUS_PROCEDURE, toDbStatus(status));
    }

    @Override
    public List<SalesOrder> findByCustomerVatNumber(String vatNumber) throws DaoException {
        if (vatNumber == null || vatNumber.isBlank()) throw new DaoException("Customer VAT number is required");
        return findOrdersByProcedure(FIND_BY_CUSTOMER_PROCEDURE, vatNumber.trim());
    }

    private List<SalesOrder> findOrdersByProcedure(String procedure, String parameter) throws DaoException {
        Connection connection = connection();
        List<SalesOrder> orders = new ArrayList<>();

        try (CallableStatement statement = connection.prepareCall(procedure)) {
            statement.setString(1, parameter);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) orders.add(mapOrder(resultSet));
            }
            for (SalesOrder order : orders) order.setItems(findItems(connection, order.getOrderId()));
            return orders;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve filtered sales orders", e);
        }
    }

    private List<SalesOrderItem> findItems(Connection connection, int orderId) throws SQLException, DaoException {
        List<SalesOrderItem> items = new ArrayList<>();

        try (CallableStatement statement = connection.prepareCall(FIND_ORDER_ITEMS_PROCEDURE)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        items.add(new SalesOrderItem(
                            resultSet.getString("CodiceSpecie"),
                            resultSet.getInt("Quantita"),
                            resultSet.getBigDecimal("PrezzoUnitario")
                        ));
                    } catch (RuntimeException e) {
                        throw new DaoException("Invalid sales order item data returned by the database", e);
                    }
                }
            }
        }

        return items;
    }

    private SalesOrder mapOrder(ResultSet resultSet) throws SQLException, DaoException {
        try {
            SalesOrder order = new SalesOrder(
                resultSet.getInt("IdOrdine"),
                resultSet.getString("PartitaIVA"),
                resultSet.getDate("DataOrdine").toLocalDate(),
                fromDbStatus(resultSet.getString("Stato"))
            );
            order.setDeliveryStreet(resultSet.getString("ViaConsegna"));
            order.setDeliveryCity(resultSet.getString("CittaConsegna"));
            order.setDeliveryPostalCode(resultSet.getString("CAPConsegna"));
            order.setContactPerson(resultSet.getString("Referente"));
            order.setCourierContact(normalizePersistedPhone(resultSet.getString("RecapitoCorriere")));
            return order;
        } catch (RuntimeException e) {
            throw new DaoException("Invalid sales order data returned by the database", e);
        }
    }

    private String serializeItems(List<SalesOrderItem> items) throws DaoException {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (SalesOrderItem item : items) {
            if (item == null) throw new DaoException("Sales order item is required");
            BigDecimal price = item.getUnitPrice();
            joiner.add("{\"speciesCode\":\"" + escapeJson(item.getSpeciesCode()) + "\",\"quantity\":" + item.getQuantity() + ",\"unitPrice\":" + price.toPlainString() + "}");
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
        if (status == null) throw new DaoException("Database returned a null sales order status");
        return switch (status) {
            case "APERTO" -> OrderStatus.OPEN;
            case "CONFERMATO" -> OrderStatus.CONFIRMED;
            case "SPEDITO" -> OrderStatus.SHIPPED;
            case "CONSEGNATO" -> OrderStatus.DELIVERED;
            case "ANNULLATO" -> OrderStatus.CANCELLED;
            default -> throw new DaoException("Unsupported database sales order status: " + status);
        };
    }

    private String normalizePersistedPhone(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        boolean international = trimmed.startsWith("+");
        String digits = trimmed.replaceAll("\\D", "");
        return international ? "+" + digits : digits;
    }

    private void validateOrderForPersistence(SalesOrder order) throws DaoException {
        if (order == null) throw new DaoException("Sales order is required");
        if (order.getCustomerVatNumber() == null || order.getCustomerVatNumber().isBlank()) throw new DaoException("Customer VAT number is required");
        if (order.getOrderDate() == null) throw new DaoException("Order date is required");
        if (order.getStatus() == null) throw new DaoException("Order status is required");
        if (order.getDeliveryStreet() == null || order.getDeliveryStreet().isBlank()) throw new DaoException("Delivery street is required");
        if (order.getDeliveryCity() == null || order.getDeliveryCity().isBlank()) throw new DaoException("Delivery city is required");
        if (order.getDeliveryPostalCode() == null || order.getDeliveryPostalCode().isBlank()) throw new DaoException("Delivery postal code is required");
        if (order.getContactPerson() == null || order.getContactPerson().isBlank()) throw new DaoException("Contact person is required");
        if (order.getCourierContact() == null || order.getCourierContact().isBlank()) throw new DaoException("Courier contact is required");
    }

    private void requireOrderId(int orderId) throws DaoException {
        if (orderId <= 0) throw new DaoException("Invalid sales order id");
    }
}
