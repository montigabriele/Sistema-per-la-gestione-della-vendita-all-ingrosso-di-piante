package it.verde.persistence.jdbc;

import it.verde.exception.DaoException;
import it.verde.model.entity.Address;
import it.verde.model.entity.Contact;
import it.verde.model.entity.RetailCompany;
import it.verde.model.type.AddressType;
import it.verde.model.type.ContactType;
import it.verde.persistence.connection.ConnectionProvider;
import it.verde.persistence.dao.RetailCompanyDao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public final class JdbcRetailCompanyDao extends AbstractJdbcDao implements RetailCompanyDao {
    private static final String INSERT_COMPANY_PROCEDURE = "{CALL InserisciAziendaRivenditrice(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    private static final String UPDATE_COMPANY_PROCEDURE = "{CALL ModificaAziendaRivenditrice(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    private static final String DELETE_COMPANY_PROCEDURE = "{CALL EliminaAziendaRivenditrice(?)}";
    private static final String FIND_COMPANY_PROCEDURE = "{CALL RicercaAziendaPerPartitaIVA(?)}";
    private static final String FIND_ALL_COMPANIES_PROCEDURE = "{CALL VisualizzaAziendeClienti()}";
    private static final String EXISTS_BY_VAT_PROCEDURE = "{CALL VerificaAziendaRivenditriceEsistente(?)}";
    private static final String FIND_CONTACTS_PROCEDURE = "{CALL VisualizzaContattiAzienda(?)}";

    public JdbcRetailCompanyDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public String create(RetailCompany company) throws DaoException {
        validateCompany(company);
        try {
            executeCompanyProcedure(connection(), INSERT_COMPANY_PROCEDURE, company);
            return company.getVatNumber();
        } catch (SQLException e) {
            throw dataAccessException("Unable to create retail company", e);
        }
    }

    @Override
    public Optional<RetailCompany> findByVatNumber(String vatNumber) throws DaoException {
        if (vatNumber == null || vatNumber.isBlank()) return Optional.empty();
        Connection connection = connection();
        RetailCompany company;

        try (CallableStatement statement = connection.prepareCall(FIND_COMPANY_PROCEDURE)) {
            statement.setString(1, vatNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) return Optional.empty();
                company = mapCompany(resultSet);
            }
            company.setContacts(findContacts(connection, vatNumber));
            return Optional.of(company);
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve retail company", e);
        }
    }

    @Override
    public List<RetailCompany> findAll() throws DaoException {
        Connection connection = connection();
        List<RetailCompany> companies = new ArrayList<>();

        try (CallableStatement statement = connection.prepareCall(FIND_ALL_COMPANIES_PROCEDURE); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) companies.add(mapCompany(resultSet));
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve retail companies", e);
        }

        try {
            for (RetailCompany company : companies) company.setContacts(findContacts(connection, company.getVatNumber()));
            return companies;
        } catch (SQLException e) {
            throw dataAccessException("Unable to retrieve retail company contacts", e);
        }
    }

    @Override
    public void update(RetailCompany company) throws DaoException {
        validateCompany(company);
        try {
            executeCompanyProcedure(connection(), UPDATE_COMPANY_PROCEDURE, company);
        } catch (SQLException e) {
            throw dataAccessException("Unable to update retail company", e);
        }
    }

    @Override
    public boolean deleteByVatNumber(String vatNumber) throws DaoException {
        if (vatNumber == null || vatNumber.isBlank()) throw new DaoException("VAT number is required");

        try (CallableStatement statement = connection().prepareCall(DELETE_COMPANY_PROCEDURE)) {
            statement.setString(1, vatNumber);
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw dataAccessException("Unable to delete retail company", e);
        }
    }

    @Override
    public boolean existsByVatNumber(String vatNumber) throws DaoException {
        if (vatNumber == null || vatNumber.isBlank()) return false;

        try (CallableStatement statement = connection().prepareCall(EXISTS_BY_VAT_PROCEDURE)) {
            statement.setString(1, vatNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean("Esiste");
            }
        } catch (SQLException e) {
            throw dataAccessException("Unable to verify retail company", e);
        }
    }

    private void executeCompanyProcedure(Connection connection, String procedure, RetailCompany company) throws SQLException, DaoException {
        try (CallableStatement statement = connection.prepareCall(procedure)) {
            Address legal = company.getLegalAddress();
            Address billing = company.getBillingAddress();
            statement.setString(1, company.getVatNumber());
            statement.setString(2, company.getCompanyName());
            statement.setString(3, company.getContactFirstName());
            statement.setString(4, company.getContactLastName());
            statement.setString(5, legal.getStreet());
            statement.setString(6, legal.getPostalCode());
            statement.setString(7, legal.getCity());
            statement.setString(8, billing.getStreet());
            statement.setString(9, billing.getPostalCode());
            statement.setString(10, billing.getCity());
            statement.setString(11, serializeContacts(company.getContacts()));
            statement.execute();
        }
    }

    private RetailCompany mapCompany(ResultSet resultSet) throws SQLException {
        Address legal = new Address(AddressType.LEGAL, resultSet.getString("ViaLegale"), resultSet.getString("CapLegale"), resultSet.getString("CittaLegale"));
        Address billing = new Address(AddressType.BILLING, resultSet.getString("ViaFatturazione"), resultSet.getString("CapFatturazione"), resultSet.getString("CittaFatturazione"));
        return new RetailCompany(resultSet.getString("PartitaIVA"), resultSet.getString("NomeAzienda"), resultSet.getString("NomeReferente"), resultSet.getString("CognomeReferente"), legal, billing);
    }

    private List<Contact> findContacts(Connection connection, String vatNumber) throws SQLException, DaoException {
        List<Contact> contacts = new ArrayList<>();

        try (CallableStatement statement = connection.prepareCall(FIND_CONTACTS_PROCEDURE)) {
            statement.setString(1, vatNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ContactType type = fromDbContactType(resultSet.getString("TipoContatto"));
                    String value = normalizePersistedContact(type, resultSet.getString("ValoreContatto"));
                    contacts.add(new Contact(type, value));
                }
            }
        }

        return contacts;
    }

    private String serializeContacts(List<Contact> contacts) throws DaoException {
        if (contacts == null || contacts.isEmpty()) throw new DaoException("At least one contact is required");

        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (Contact contact : contacts) {
            if (contact == null || contact.type() == null) throw new DaoException("Contact type is required");
            if (contact.value() == null || contact.value().isBlank()) throw new DaoException("Contact value is required");
            joiner.add("{\"type\":\"" + toDbContactType(contact.type()) + "\",\"value\":\"" + escapeJson(contact.value().trim()) + "\"}");
        }
        return joiner.toString();
    }

    private String escapeJson(String value) {
        StringBuilder escaped = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '\\' -> escaped.append("\\\\");
                case '"' -> escaped.append("\\\"");
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

    private String toDbContactType(ContactType type) {
        return switch (type) {
            case PHONE -> "TELEFONO";
            case MOBILE -> "CELLULARE";
            case EMAIL -> "EMAIL";
        };
    }

    private ContactType fromDbContactType(String value) throws DaoException {
        if (value == null) throw new DaoException("Database returned a null contact type");
        return switch (value) {
            case "TELEFONO" -> ContactType.PHONE;
            case "CELLULARE" -> ContactType.MOBILE;
            case "EMAIL" -> ContactType.EMAIL;
            default -> throw new DaoException("Unsupported database contact type: " + value);
        };
    }

    private String normalizePersistedContact(ContactType type, String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        if (type == ContactType.EMAIL) return trimmed;
        boolean international = trimmed.startsWith("+");
        String digits = trimmed.replaceAll("\\D", "");
        return international ? "+" + digits : digits;
    }

    private void validateCompany(RetailCompany company) throws DaoException {
        if (company == null) throw new DaoException("Retail company is required");
        if (company.getVatNumber() == null || company.getVatNumber().isBlank()) throw new DaoException("VAT number is required");
        if (company.getLegalAddress() == null) throw new DaoException("Legal address is required");
        if (company.getBillingAddress() == null) throw new DaoException("Billing address is required");
        if (company.getContacts().isEmpty()) throw new DaoException("At least one contact is required");
    }
}
