package it.verde.mapper;

import it.verde.model.entity.Supplier;
import it.verde.view.bean.SupplierBean;

import java.util.Objects;

public final class SupplierMapper {
    private final AddressMapper addressMapper;

    public SupplierMapper() {
        this(new AddressMapper());
    }

    public SupplierMapper(AddressMapper addressMapper) {
        this.addressMapper = Objects.requireNonNull(addressMapper);
    }

    public Supplier toEntity(SupplierBean bean) {
        Objects.requireNonNull(bean, "Supplier bean cannot be null");
        Supplier entity = new Supplier(bean.getSupplierId(), bean.getName(), bean.getTaxCode());
        entity.setAddresses(bean.getAddresses().stream().map(addressMapper::toEntity).toList());
        return entity;
    }

    public SupplierBean toBean(Supplier entity) {
        Objects.requireNonNull(entity, "Supplier entity cannot be null");
        SupplierBean bean = new SupplierBean();
        bean.setSupplierId(entity.getSupplierId());
        bean.setName(entity.getName());
        bean.setTaxCode(entity.getTaxCode());
        bean.setAddresses(entity.getAddresses().stream().map(addressMapper::toBean).toList());
        return bean;
    }
}
