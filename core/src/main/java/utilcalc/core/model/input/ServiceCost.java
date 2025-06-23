package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.*;

import java.math.BigDecimal;
import utilcalc.core.model.DateRange;

public record ServiceCost(DateRange dateRange, BigDecimal annualCost) {
    public ServiceCost {
        ensureNonNull(annualCost, "annualCost");
    }
}
