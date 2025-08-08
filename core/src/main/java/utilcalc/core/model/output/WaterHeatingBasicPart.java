package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.ensureNonNull;

import java.math.BigDecimal;
import utilcalc.core.model.DateRange;

public record WaterHeatingBasicPart(
        DateRange dateRange,
        BigDecimal numberOfMonths,
        BigDecimal monthlyCost,
        BigDecimal totalAmount) {
    public WaterHeatingBasicPart {
        ensureNonNull(dateRange, "dateRange");
        ensureNonNull(numberOfMonths, "numberOfMonths");
        ensureNonNull(monthlyCost, "monthlyCost");
        ensureNonNull(totalAmount, "totalAmount");
    }
}
