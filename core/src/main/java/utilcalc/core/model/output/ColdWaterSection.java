package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.*;

import java.math.BigDecimal;
import java.util.List;

public record ColdWaterSection(
        String name, BigDecimal totalAmount, List<WaterReading> readings, List<WaterFee> priceList)
        implements ReportSection {

    public ColdWaterSection {
        ensureNonBlank(name, "name");
        ensureNonNull(totalAmount, "totalAmount");
        ensureNonEmpty(readings, "readings");
        ensureNonEmpty(priceList, "priceList");

        readings = List.copyOf(readings);
        priceList = List.copyOf(priceList);
    }
}
