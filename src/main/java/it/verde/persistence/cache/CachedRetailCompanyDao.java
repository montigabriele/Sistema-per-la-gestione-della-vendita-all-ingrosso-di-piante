package it.verde.persistence.cache;

import it.verde.exception.DaoException;
import it.verde.model.entity.Address;
import it.verde.model.entity.RetailCompany;
import it.verde.persistence.connection.ConnectionProvider;
import it.verde.persistence.dao.RetailCompanyDao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class CachedRetailCompanyDao implements RetailCompanyDao {
    private final RetailCompanyDao delegate;
    private final ConnectionProvider connectionProvider;
    private final Map<String, RetailCompany> companiesByVatNumber = new HashMap<>();
    private List<RetailCompany> allCompanies;
    private Connection sessionConnection;

    public CachedRetailCompanyDao(RetailCompanyDao delegate, ConnectionProvider connectionProvider) {
        this.delegate = Objects.requireNonNull(delegate, "Retail company DAO delegate cannot be null");
        this.connectionProvider = Objects.requireNonNull(connectionProvider, "Connection provider cannot be null");
    }

    @Override
    public String create(RetailCompany company) throws DaoException {
        try {
            return delegate.create(company);
        } finally {
            clearCache();
        }
    }

    @Override
    public Optional<RetailCompany> findByVatNumber(String vatNumber) throws DaoException {
        if (vatNumber == null || vatNumber.isBlank()) return delegate.findByVatNumber(vatNumber);
        ensureCurrentSession();

        RetailCompany cached = companiesByVatNumber.get(vatNumber);
        if (cached != null) return Optional.of(copyCompany(cached));

        Optional<RetailCompany> loaded = delegate.findByVatNumber(vatNumber);
        if (loaded.isEmpty()) return Optional.empty();

        RetailCompany snapshot = copyCompany(loaded.get());
        companiesByVatNumber.put(snapshot.getVatNumber(), snapshot);
        return Optional.of(copyCompany(snapshot));
    }

    @Override
    public List<RetailCompany> findAll() throws DaoException {
        ensureCurrentSession();
        if (allCompanies != null) return copyCompanies(allCompanies);

        List<RetailCompany> loaded = delegate.findAll();
        List<RetailCompany> snapshots = copyCompanies(loaded);
        allCompanies = snapshots;

        for (RetailCompany company : snapshots) {
            if (company.getVatNumber() != null) companiesByVatNumber.put(company.getVatNumber(), copyCompany(company));
        }

        return copyCompanies(snapshots);
    }

    @Override
    public void update(RetailCompany company) throws DaoException {
        try {
            delegate.update(company);
        } finally {
            invalidateCompany(company == null ? null : company.getVatNumber());
        }
    }

    @Override
    public boolean deleteByVatNumber(String vatNumber) throws DaoException {
        try {
            return delegate.deleteByVatNumber(vatNumber);
        } finally {
            invalidateCompany(vatNumber);
        }
    }

    @Override
    public boolean existsByVatNumber(String vatNumber) throws DaoException {
        return delegate.existsByVatNumber(vatNumber);
    }

    public void clearCache() {
        companiesByVatNumber.clear();
        allCompanies = null;
    }

    private void ensureCurrentSession() throws DaoException {
        Connection currentConnection = connectionProvider.getConnection();
        if (currentConnection == sessionConnection) return;
        clearCache();
        sessionConnection = currentConnection;
    }

    private void invalidateCompany(String vatNumber) {
        if (vatNumber == null) companiesByVatNumber.clear();
        else companiesByVatNumber.remove(vatNumber);
        allCompanies = null;
    }

    private List<RetailCompany> copyCompanies(List<RetailCompany> companies) {
        List<RetailCompany> copies = new ArrayList<>(companies.size());
        for (RetailCompany company : companies) copies.add(copyCompany(company));
        return copies;
    }

    private RetailCompany copyCompany(RetailCompany company) {
        Address legalAddress = copyAddress(company.getLegalAddress());
        Address billingAddress = company.getBillingAddress() == company.getLegalAddress() ? legalAddress : copyAddress(company.getBillingAddress());
        RetailCompany copy = new RetailCompany(company.getVatNumber(), company.getCompanyName(), company.getContactFirstName(), company.getContactLastName(), legalAddress, billingAddress);
        copy.setContacts(company.getContacts());
        return copy;
    }

    private Address copyAddress(Address address) {
        if (address == null) return null;
        return new Address(address.getId(), address.getType(), address.getStreet(), address.getPostalCode(), address.getCity());
    }
}
