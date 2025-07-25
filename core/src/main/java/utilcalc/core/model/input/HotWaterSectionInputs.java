package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.ensureNonBlank;
import static utilcalc.core.utils.Util.ensureNonEmpty;

import java.util.List;

public record HotWaterSectionInputs(
        String name,
        List<MeterReading> readings,
        List<WaterTariff> priceList,
        List<ServiceCost> heatingBasicCosts,
        List<WaterTariff> heatingConsumableTariffs)
        implements SectionInputs {
    public HotWaterSectionInputs {
        ensureNonBlank(name, "name");
        ensureNonEmpty(readings, "readings");
        ensureNonEmpty(priceList, "priceList");
        ensureNonEmpty(heatingBasicCosts, "heatingBasicComponents");
        ensureNonEmpty(heatingConsumableTariffs, "heatingConsumableComponents");

        readings = List.copyOf(readings);
        priceList = List.copyOf(priceList);
        heatingBasicCosts = List.copyOf(heatingBasicCosts);
        heatingConsumableTariffs = List.copyOf(heatingConsumableTariffs);
    }
}
