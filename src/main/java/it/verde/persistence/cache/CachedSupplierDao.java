package it.verde.persistence.cache;

import it.verde.exception.DaoException;
import it.verde.model.entity.Address;
import it.verde.model.entity.PlantSpecies;
import it.verde.model.entity.Supplier;
import it.verde.persistence.connection.ConnectionProvider;
import it.verde.persistence.dao.SupplierDao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class CachedSupplierDao implements SupplierDao {
    private final SupplierDao delegate;
    private final ConnectionProvider connectionProvider;
    private final Map<Integer, Supplier> suppliersById = new HashMap<>();
    private List<Supplier> allSuppliers;
    private Connection sessionConnection;

    public CachedSupplierDao(SupplierDao delegate, ConnectionProvider connectionProvider) {
        this.delegate = Objects.requireNonNull(delegate, "Supplier DAO delegate cannot be null");
        this.connectionProvider = Objects.requireNonNull(connectionProvider, "Connection provider cannot be null");
    }

    @Override
    public Integer create(Supplier supplier) throws DaoException {
        try {
            return delegate.create(supplier);
        } finally {
            clearCache();
        }
    }

    @Override
    public Optional<Supplier> findById(Integer supplierId) throws DaoException {
        if (supplierId == null || supplierId <= 0) return delegate.findById(supplierId);
        ensureCurrentSession();

        Supplier cached = suppliersById.get(supplierId);
        if (cached != null) return Optional.of(copySupplier(cached));

        Optional<Supplier> loaded = delegate.findById(supplierId);
        if (loaded.isEmpty()) return Optional.empty();

        Supplier snapshot = copySupplier(loaded.get());
        suppliersById.put(supplierId, snapshot);
        return Optional.of(copySupplier(snapshot));
    }

    @Override
    public List<Supplier> findAll() throws DaoException {
        ensureCurrentSession();
        if (allSuppliers != null) return copySuppliers(allSuppliers);

        List<Supplier> loaded = delegate.findAll();
        List<Supplier> snapshots = copySuppliers(loaded);
        allSuppliers = snapshots;

        for (Supplier supplier : snapshots) {
            if (supplier.getSupplierId() != null) suppliersById.put(supplier.getSupplierId(), copySupplier(supplier));
        }

        return copySuppliers(snapshots);
    }

    @Override
    public void update(Supplier supplier) throws DaoException {
        try {
            delegate.update(supplier);
        } finally {
            invalidateSupplier(supplier == null ? null : supplier.getSupplierId());
        }
    }

    @Override
    public boolean deleteById(Integer supplierId) throws DaoException {
        try {
            return delegate.deleteById(supplierId);
        } finally {
            invalidateSupplier(supplierId);
        }
    }

    @Override
    public boolean existsById(Integer supplierId) throws DaoException {
        return delegate.existsById(supplierId);
    }

    @Override
    public void addSuppliedSpecies(Integer supplierId, String speciesCode) throws DaoException {
        delegate.addSuppliedSpecies(supplierId, speciesCode);
    }

    @Override
    public void removeSuppliedSpecies(Integer supplierId, String speciesCode) throws DaoException {
        delegate.removeSuppliedSpecies(supplierId, speciesCode);
    }

    @Override
    public List<String> findSuppliedSpeciesCodes(Integer supplierId) throws DaoException {
        return delegate.findSuppliedSpeciesCodes(supplierId);
    }

    @Override
    public boolean suppliesSpecies(Integer supplierId, String speciesCode) throws DaoException {
        return delegate.suppliesSpecies(supplierId, speciesCode);
    }

    @Override
    public List<PlantSpecies> findSuppliedSpecies(Integer supplierId) throws DaoException {
        return delegate.findSuppliedSpecies(supplierId);
    }

    public void clearCache() {
        suppliersById.clear();
        allSuppliers = null;
    }

    private void ensureCurrentSession() throws DaoException {
        Connection currentConnection = connectionProvider.getConnection();
        if (currentConnection == sessionConnection) return;
        clearCache();
        sessionConnection = currentConnection;
    }

    private void invalidateSupplier(Integer supplierId) {
        if (supplierId == null) suppliersById.clear();
        else suppliersById.remove(supplierId);
        allSuppliers = null;
    }

    private List<Supplier> copySuppliers(List<Supplier> suppliers) {
        List<Supplier> copies = new ArrayList<>(suppliers.size());
        for (Supplier supplier : suppliers) copies.add(copySupplier(supplier));
        return copies;
    }

    private Supplier copySupplier(Supplier supplier) {
        Supplier copy = new Supplier(supplier.getSupplierId(), supplier.getName(), supplier.getTaxCode());
        List<Address> addresses = new ArrayList<>();
        for (Address address : supplier.getAddresses()) addresses.add(copyAddress(address));
        copy.setAddresses(addresses);
        return copy;
    }

    private Address copyAddress(Address address) {
        return new Address(address.getId(), address.getType(), address.getStreet(), address.getPostalCode(), address.getCity());
    }
}
