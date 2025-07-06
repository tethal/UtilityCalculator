package utilcalc.core.model.output;

import utilcalc.core.model.DateRange;

import java.math.BigDecimal;

import static utilcalc.core.utils.Util.ensureNonNull;

public record ColdWaterReading(DateRange dateRange, BigDecimal startState, BigDecimal endState, BigDecimal consumption) {
    public ColdWaterReading {
        ensureNonNull(dateRange, "dateRange");
        ensureNonNull(startState, "startState");
        ensureNonNull(endState, "endState");
        ensureNonNull(consumption, "consumption");
    }
}
