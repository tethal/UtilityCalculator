package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.ensureNonBlank;
import static utilcalc.core.utils.Util.ensureNonEmpty;

import java.util.List;

public record ColdWaterSectionInputs(
        String name, List<MeterReading> readings, List<WaterTariff> priceList)
        implements SectionInputs {
    public ColdWaterSectionInputs {
        ensureNonBlank(name, "name");
        ensureNonEmpty(readings, "readings");
        ensureNonEmpty(priceList, "priceList");

        readings = List.copyOf(readings);
        priceList = List.copyOf(priceList);
    }
}
