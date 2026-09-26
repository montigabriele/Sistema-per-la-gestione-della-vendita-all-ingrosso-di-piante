package it.verde.mapper;

import it.verde.model.entity.PriceHistory;
import it.verde.view.bean.PriceHistoryBean;

import java.util.Objects;

public final class PriceHistoryMapper {
    public PriceHistory toEntity(PriceHistoryBean bean) {
        Objects.requireNonNull(bean, "Price history bean cannot be null");
        return new PriceHistory(bean.getChangedAt(), bean.getPreviousPrice(), bean.getCurrentPrice(), bean.getSpeciesCode());
    }

    public PriceHistoryBean toBean(PriceHistory entity) {
        Objects.requireNonNull(entity, "Price history entity cannot be null");
        PriceHistoryBean bean = new PriceHistoryBean();
        bean.setChangedAt(entity.getChangedAt());
        bean.setPreviousPrice(entity.getPreviousPrice());
        bean.setCurrentPrice(entity.getCurrentPrice());
        bean.setSpeciesCode(entity.getSpeciesCode());
        return bean;
    }
}
