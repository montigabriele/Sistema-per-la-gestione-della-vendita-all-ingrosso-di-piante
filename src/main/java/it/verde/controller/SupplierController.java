package it.verde.controller;

import it.verde.exception.ApplicationException;
import it.verde.exception.DaoException;
import it.verde.mapper.PlantSpeciesMapper;
import it.verde.mapper.SupplierMapper;
import it.verde.model.entity.Supplier;
import it.verde.persistence.dao.SupplierDao;
import it.verde.view.bean.PlantSpeciesBean;
import it.verde.view.bean.SupplierBean;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class SupplierController {
    private final SupplierDao supplierDao;
    private final SupplierMapper supplierMapper;
    private final PlantSpeciesMapper plantSpeciesMapper;

    public SupplierController(SupplierDao supplierDao, SupplierMapper supplierMapper, PlantSpeciesMapper plantSpeciesMapper) {
        this.supplierDao = Objects.requireNonNull(supplierDao);
        this.supplierMapper = Objects.requireNonNull(supplierMapper);
        this.plantSpeciesMapper = Objects.requireNonNull(plantSpeciesMapper);
    }

    public Integer createSupplier(SupplierBean bean) throws ApplicationException {
        Supplier supplier = mapAndValidate(bean);
        try {
            return supplierDao.create(supplier);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to create supplier", e);
        }
    }

    public List<SupplierBean> getAllSuppliers() throws ApplicationException {
        try {
            return supplierDao.findAll().stream().map(supplierMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve suppliers", e);
        }
    }

    public Optional<SupplierBean> getSupplierById(Integer supplierId) throws ApplicationException {
        requireSupplierId(supplierId);
        try {
            return supplierDao.findById(supplierId).map(supplierMapper::toBean);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve supplier", e);
        }
    }

    public boolean updateSupplier(SupplierBean bean) throws ApplicationException {
        Supplier supplier = mapAndValidate(bean);
        requireSupplierId(supplier.getSupplierId());
        if (!existsById(supplier.getSupplierId())) throw new ApplicationException("Supplier not found with id: " + supplier.getSupplierId());
        try {
            supplierDao.update(supplier);
            return true;
        } catch (DaoException e) {
            throw new ApplicationException("Unable to update supplier", e);
        }
    }

    public boolean deleteSupplier(Integer supplierId) throws ApplicationException {
        requireSupplierId(supplierId);
        try {
            return supplierDao.deleteById(supplierId);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to delete supplier", e);
        }
    }

    public void addSuppliedSpecies(Integer supplierId, String speciesCode) throws ApplicationException {
        requireSupplierId(supplierId);
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        if (!existsById(supplierId)) throw new ApplicationException("Supplier not found with id: " + supplierId);
        if (suppliesSpecies(supplierId, normalizedCode)) throw new ApplicationException("The supplier already supplies this species");
        try {
            supplierDao.addSuppliedSpecies(supplierId, normalizedCode);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to add supplied species", e);
        }
    }

    public void removeSuppliedSpecies(Integer supplierId, String speciesCode) throws ApplicationException {
        requireSupplierId(supplierId);
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        if (!suppliesSpecies(supplierId, normalizedCode)) throw new ApplicationException("The supplier does not supply the specified species");
        try {
            supplierDao.removeSuppliedSpecies(supplierId, normalizedCode);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to remove supplied species", e);
        }
    }

    public boolean suppliesSpecies(Integer supplierId, String speciesCode) throws ApplicationException {
        requireSupplierId(supplierId);
        String normalizedCode = normalizeSpeciesCode(speciesCode);
        try {
            return supplierDao.suppliesSpecies(supplierId, normalizedCode);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify supplied species", e);
        }
    }

    public List<String> getSuppliedSpeciesCodes(Integer supplierId) throws ApplicationException {
        requireSupplierId(supplierId);
        if (!existsById(supplierId)) throw new ApplicationException("Supplier not found with id: " + supplierId);
        try {
            return supplierDao.findSuppliedSpeciesCodes(supplierId);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve supplied species codes", e);
        }
    }

    public boolean existsById(Integer supplierId) throws ApplicationException {
        try {
            return supplierDao.existsById(supplierId);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify supplier", e);
        }
    }

    public List<PlantSpeciesBean> getSuppliedSpecies(Integer supplierId) throws ApplicationException {
        requireSupplierId(supplierId);
        if (!existsById(supplierId)) throw new ApplicationException("Supplier not found with id: " + supplierId);
        try {
            return supplierDao.findSuppliedSpecies(supplierId).stream().map(plantSpeciesMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve supplied species", e);
        }
    }

    private Supplier mapAndValidate(SupplierBean bean) throws ApplicationException {
        if (bean == null) throw new ApplicationException("Supplier cannot be null");
        Supplier supplier;
        try {
            supplier = supplierMapper.toEntity(bean);
        } catch (RuntimeException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
        if (isBlank(supplier.getName())) throw new ApplicationException("Supplier name is required");
        if (isBlank(supplier.getTaxCode())) throw new ApplicationException("Supplier tax code is required");
        return supplier;
    }

    private void requireSupplierId(Integer supplierId) throws ApplicationException {
        if (supplierId == null || supplierId <= 0) throw new ApplicationException("Invalid supplier id");
    }

    private String normalizeSpeciesCode(String speciesCode) throws ApplicationException {
        if (speciesCode == null || speciesCode.trim().isEmpty()) throw new ApplicationException("Species code is required");
        return speciesCode.trim().toUpperCase();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
