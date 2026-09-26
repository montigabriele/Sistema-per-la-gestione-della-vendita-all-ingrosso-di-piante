package it.verde.mapper;

import it.verde.model.entity.Address;
import it.verde.model.entity.RetailCompany;
import it.verde.view.bean.RetailCompanyBean;

import java.util.Objects;

public final class RetailCompanyMapper {
    private final AddressMapper addressMapper;
    private final ContactMapper contactMapper;

    public RetailCompanyMapper() {
        this(new AddressMapper(), new ContactMapper());
    }

    public RetailCompanyMapper(AddressMapper addressMapper, ContactMapper contactMapper) {
        this.addressMapper = Objects.requireNonNull(addressMapper);
        this.contactMapper = Objects.requireNonNull(contactMapper);
    }

    public RetailCompany toEntity(RetailCompanyBean bean) {
        Objects.requireNonNull(bean, "Retail company bean cannot be null");
        Address legalAddress = addressMapper.toEntity(Objects.requireNonNull(bean.getLegalAddress(), "Legal address cannot be null"));
        Address billingAddress = bean.getBillingAddress() == null ? null : addressMapper.toEntity(bean.getBillingAddress());
        RetailCompany entity = new RetailCompany(bean.getVatNumber(), bean.getCompanyName(), bean.getContactFirstName(), bean.getContactLastName(), legalAddress, billingAddress);
        entity.setContacts(bean.getContacts().stream().map(contactMapper::toEntity).toList());
        return entity;
    }

    public RetailCompanyBean toBean(RetailCompany entity) {
        Objects.requireNonNull(entity, "Retail company entity cannot be null");
        RetailCompanyBean bean = new RetailCompanyBean();
        bean.setVatNumber(entity.getVatNumber());
        bean.setCompanyName(entity.getCompanyName());
        bean.setContactFirstName(entity.getContactFirstName());
        bean.setContactLastName(entity.getContactLastName());
        bean.setLegalAddress(addressMapper.toBean(entity.getLegalAddress()));
        bean.setBillingAddress(entity.getBillingAddress() == null ? null : addressMapper.toBean(entity.getBillingAddress()));
        bean.setContacts(entity.getContacts().stream().map(contactMapper::toBean).toList());
        return bean;
    }
}
