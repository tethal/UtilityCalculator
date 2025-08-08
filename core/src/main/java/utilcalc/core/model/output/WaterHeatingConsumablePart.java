package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.ensureNonNull;

import java.math.BigDecimal;
import utilcalc.core.model.DateRange;

public record WaterHeatingConsumablePart(
        DateRange dateRange, BigDecimal unitAmount, BigDecimal unitCost, BigDecimal totalCost) {
    public WaterHeatingConsumablePart {
        ensureNonNull(dateRange, "dateRange");
        ensureNonNull(unitAmount, "unitAmount");
        ensureNonNull(unitCost, "unitCost");
        ensureNonNull(totalCost, "totalCost");
    }
}
