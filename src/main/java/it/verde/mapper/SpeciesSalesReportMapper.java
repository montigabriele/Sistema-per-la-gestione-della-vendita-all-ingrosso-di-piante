package it.verde.mapper;

import it.verde.model.readmodel.SpeciesSalesReport;
import it.verde.view.bean.SpeciesSalesReportBean;

import java.util.Objects;

public final class SpeciesSalesReportMapper {
    public SpeciesSalesReportBean toBean(SpeciesSalesReport model) {
        Objects.requireNonNull(model, "Species sales report cannot be null");
        SpeciesSalesReportBean bean = new SpeciesSalesReportBean();
        bean.setSpeciesCode(model.speciesCode());
        bean.setCommonName(model.commonName());
        bean.setPlantType(model.plantType() == null ? null : model.plantType().name());
        bean.setFlowering(model.flowering());
        bean.setSoldQuantity(model.soldQuantity());
        bean.setTotalSalesValue(model.totalSalesValue());
        return bean;
    }
}
