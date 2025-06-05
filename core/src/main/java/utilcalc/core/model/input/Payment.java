package utilcalc.core.model.input;

import java.math.BigDecimal;

public final class Payment {
    private final String description;
    private final BigDecimal count;
    private final BigDecimal unitAmount;

    public Payment(String description, BigDecimal count, BigDecimal unitAmount) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("description cannot be blank");
        }
        if (count == null) {
            throw new IllegalArgumentException("count cannot be null");
        }
        if (unitAmount == null) {
            throw new IllegalArgumentException("unitAmount cannot be null");
        }

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
