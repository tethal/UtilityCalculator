package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.ensureNonNull;

import java.math.BigDecimal;
import utilcalc.core.model.DateRange;

public record ColdWaterFee(
        DateRange dateRange, BigDecimal quantity, BigDecimal unitAmount, BigDecimal periodAmount) {
    public ColdWaterFee {
        ensureNonNull(dateRange, "dateRange");
        ensureNonNull(quantity, "quantity");
        ensureNonNull(unitAmount, "unitAmount");
        ensureNonNull(periodAmount, "periodAmount");
    }
}
