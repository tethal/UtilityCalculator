package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.*;

import java.math.BigDecimal;
import utilcalc.core.model.DateRange;

public record OtherFee(
        DateRange dateRange, BigDecimal monthlyCost, BigDecimal monthCount, BigDecimal feeAmount) {
    public OtherFee {
        ensureNonNull(dateRange, "dateRange");
        ensureNonNull(monthlyCost, "monthlyCost");
        ensureNonNull(monthCount, "monthCount");
        ensureNonNull(feeAmount, "feeAmount");
    }
}
