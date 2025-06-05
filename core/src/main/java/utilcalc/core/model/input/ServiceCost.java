package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class ServiceCost {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final BigDecimal annualCost;

    public ServiceCost(LocalDate startDate, LocalDate endDate, BigDecimal annualCost) {
        ensureNonNull(startDate, "startDate");
        ensureNonNull(endDate, "endDate");
        ensureNonNull(annualCost, "annualCost");
        ensureValidDateRange(startDate, endDate);

        this.startDate = startDate;
        this.endDate = endDate;
        this.annualCost = annualCost;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getAnnualCost() {
        return annualCost;
    }

    @Override
    public String toString() {
        return "ServiceCost [startDate="
                + startDate
                + ", endDate="
                + endDate
                + ", annualCost="
                + annualCost
                + "]";
    }
}
