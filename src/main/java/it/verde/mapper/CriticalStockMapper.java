package it.verde.mapper;

import it.verde.model.readmodel.CriticalStock;
import it.verde.view.bean.CriticalStockBean;

import java.util.Objects;

public final class CriticalStockMapper {
    public CriticalStockBean toBean(CriticalStock model) {
        Objects.requireNonNull(model, "Critical stock cannot be null");
        CriticalStockBean bean = new CriticalStockBean();
        bean.setSpeciesCode(model.speciesCode());
        bean.setCommonName(model.commonName());
        bean.setLatinName(model.latinName());
        bean.setQuantity(model.quantity());
        return bean;
    }
}
