package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.ensureNonNull;

import java.math.BigDecimal;
import java.time.YearMonth;

public record HeatingFee(
        YearMonth yearMonth,
        BigDecimal annualCost,
        BigDecimal monthCount,
        BigDecimal coefficient,
        BigDecimal feeAmount) {
    public HeatingFee {
        ensureNonNull(yearMonth, "yearMonth");
        ensureNonNull(annualCost, "annualCost");
        ensureNonNull(monthCount, "monthCount");
        ensureNonNull(coefficient, "coefficient");
        ensureNonNull(feeAmount, "feeAmount");
    }
}
