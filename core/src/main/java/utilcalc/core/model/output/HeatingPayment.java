package utilcalc.core.model.output;

import java.math.BigDecimal;
import utilcalc.core.model.Payment;

/**
 * Represents a single heating payment
 *
 * <p>This class is immutable
 */
public final class HeatingPayment extends Payment {

    /** Coefficient used to calculate the specific heating cost for a specific month */
    private final BigDecimal coefficient;

    public HeatingPayment(
            String description,
            BigDecimal count,
            BigDecimal unitAmount,
            BigDecimal amount,
            BigDecimal coefficient) {
        super(description, count, unitAmount, amount);
        this.coefficient = coefficient;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }
}
