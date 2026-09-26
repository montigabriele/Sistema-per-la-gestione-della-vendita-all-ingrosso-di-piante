package it.verde.controller;

import it.verde.exception.ApplicationException;
import it.verde.exception.DaoException;
import it.verde.mapper.RetailCompanyMapper;
import it.verde.model.entity.Address;
import it.verde.model.entity.Contact;
import it.verde.model.entity.RetailCompany;
import it.verde.persistence.dao.RetailCompanyDao;
import it.verde.view.bean.RetailCompanyBean;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class RetailCompanyController {
    private final RetailCompanyDao retailCompanyDao;
    private final RetailCompanyMapper retailCompanyMapper;

    public RetailCompanyController(RetailCompanyDao retailCompanyDao, RetailCompanyMapper retailCompanyMapper) {
        this.retailCompanyDao = Objects.requireNonNull(retailCompanyDao);
        this.retailCompanyMapper = Objects.requireNonNull(retailCompanyMapper);
    }

    public List<RetailCompanyBean> getAllCompanies() throws ApplicationException {
        try {
            return retailCompanyDao.findAll().stream().map(retailCompanyMapper::toBean).toList();
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve retail companies", e);
        }
    }

    public Optional<RetailCompanyBean> getCompanyByVatNumber(String vatNumber) throws ApplicationException {
        try {
            return retailCompanyDao.findByVatNumber(vatNumber).map(retailCompanyMapper::toBean);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to retrieve retail company", e);
        }
    }

    public boolean existsByVatNumber(String vatNumber) throws ApplicationException {
        if (vatNumber == null || vatNumber.trim().isEmpty()) return false;
        try {
            return retailCompanyDao.existsByVatNumber(vatNumber);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to verify retail company", e);
        }
    }

    public String createCompany(RetailCompanyBean bean) throws ApplicationException {
        RetailCompany company = mapAndValidate(bean);
        if (existsByVatNumber(company.getVatNumber())) throw new ApplicationException("A company with VAT number " + company.getVatNumber() + " already exists");
        try {
            return retailCompanyDao.create(company);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to create retail company", e);
        }
    }

    public void updateCompany(RetailCompanyBean bean) throws ApplicationException {
        RetailCompany company = mapAndValidate(bean);
        if (!existsByVatNumber(company.getVatNumber())) throw new ApplicationException("Company with VAT number " + company.getVatNumber() + " not found");
        try {
            retailCompanyDao.update(company);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to update retail company", e);
        }
    }

    public boolean deleteCompany(String vatNumber) throws ApplicationException {
        try {
            return retailCompanyDao.deleteByVatNumber(vatNumber);
        } catch (DaoException e) {
            throw new ApplicationException("Unable to delete retail company", e);
        }
    }

    private RetailCompany mapAndValidate(RetailCompanyBean bean) throws ApplicationException {
        if (bean == null) throw new ApplicationException("Company cannot be null");
        RetailCompany company;
        try {
            company = retailCompanyMapper.toEntity(bean);
        } catch (RuntimeException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
        validateCompany(company);
        return company;
    }

    private void validateCompany(RetailCompany company) throws ApplicationException {
        if (isBlank(company.getVatNumber())) throw new ApplicationException("VAT number is required");
        if (isBlank(company.getCompanyName())) throw new ApplicationException("Company name is required");
        if (isBlank(company.getContactFirstName()) || isBlank(company.getContactLastName())) throw new ApplicationException("Contact first name and last name are required");
        if (company.getLegalAddress() == null) throw new ApplicationException("Legal address is required");
        validateAddress(company.getLegalAddress(), "legal address");
        if (company.getBillingAddress() != null) validateAddress(company.getBillingAddress(), "billing address");
        if (company.getContacts() == null || company.getContacts().isEmpty()) throw new ApplicationException("At least one contact is required");
        for (Contact contact : company.getContacts()) validateContact(contact);
    }

    private void validateAddress(Address address, String description) throws ApplicationException {
        if (isBlank(address.getStreet()) || isBlank(address.getPostalCode()) || isBlank(address.getCity())) throw new ApplicationException("All fields of the " + description + " are required");
    }

    private void validateContact(Contact contact) throws ApplicationException {
        if (contact == null || contact.type() == null) throw new ApplicationException("Contact type is required");
        if (isBlank(contact.value())) throw new ApplicationException("Contact value is required");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
