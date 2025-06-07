package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.ensureNonBlank;
import static utilcalc.core.utils.Util.ensureNonNull;

import java.math.BigDecimal;

public record HeatingFee(
        String description,
        BigDecimal annualCost,
        BigDecimal monthCount,
        BigDecimal coefficient,
        BigDecimal feeAmount) {
    public HeatingFee {
        ensureNonBlank(description, "description");
        ensureNonNull(annualCost, "annualCost");
        ensureNonNull(monthCount, "monthCount");
        ensureNonNull(coefficient, "coefficient");
        ensureNonNull(feeAmount, "feeAmount");
    }
}
