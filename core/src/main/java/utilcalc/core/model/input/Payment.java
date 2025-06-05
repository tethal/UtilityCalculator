package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.*;

import java.math.BigDecimal;

public final class Payment {
    private final String description;
    private final BigDecimal count;
    private final BigDecimal unitAmount;

    public Payment(String description, BigDecimal count, BigDecimal unitAmount) {
        ensureNonBlank(description, "description");
        ensureNonNull(count, "count");
        ensureNonNull(unitAmount, "unitAmount");

        this.description = description;
        this.count = count;
        this.unitAmount = unitAmount;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getCount() {
        return count;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    @Override
    public String toString() {
        return "Payment [description="
                + description
                + ", count="
                + count
                + ", unitAmount="
                + unitAmount
                + "]";
    }
}
