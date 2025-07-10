package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.ensureNonNull;

import java.math.BigDecimal;
import utilcalc.core.model.DateRange;

public record ColdWaterReading(
        DateRange dateRange, BigDecimal startState, BigDecimal endState, BigDecimal consumption) {
    public ColdWaterReading {
        ensureNonNull(dateRange, "dateRange");
        ensureNonNull(startState, "startState");
        ensureNonNull(endState, "endState");
        ensureNonNull(consumption, "consumption");
    }
}
