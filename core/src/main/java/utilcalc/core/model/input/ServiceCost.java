package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ServiceCost(LocalDate startDate, LocalDate endDate, BigDecimal annualCost) {
    public ServiceCost {
        ensureNonNull(startDate, "startDate");
        ensureNonNull(endDate, "endDate");
        ensureNonNull(annualCost, "annualCost");
        ensureValidDateRange(startDate, endDate);
    }
}
