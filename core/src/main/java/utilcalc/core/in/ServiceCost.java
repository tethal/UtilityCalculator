package utilcalc.core.in;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class ServiceCost {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final BigDecimal annualCost;

    public ServiceCost(LocalDate startDate, LocalDate endDate, BigDecimal annualCost) {
        if (startDate == null) throw new IllegalArgumentException("startDate cannot be null");
        if (endDate == null) throw new IllegalArgumentException("endDate cannot be null");
        if (annualCost == null) throw new IllegalArgumentException("annualCost cannot be null");
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must be before startDate");
        }

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
