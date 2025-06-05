package utilcalc.core.model;

import java.math.BigDecimal;

/**
 * Represent a single payment.
 *
 * <p>This class is immutable.
 */
public class Payment {

    /** Text representation of the payment period */
    private final String description;

    /** Number of months in payment period */
    private final BigDecimal count;

    /** Amount per month */
    private final BigDecimal unitAmount;

    /** Total amount (count * unitAmount) */
    private final BigDecimal amount;

    public Payment(String description, BigDecimal count, BigDecimal unitAmount, BigDecimal amount) {
        this.description = description;
        this.count = count;
        this.unitAmount = unitAmount;
        this.amount = amount;
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

    public BigDecimal getAmount() {
        return amount;
    }
}
