package utilcalc.core.model.output;

import java.math.BigDecimal;
import java.util.List;
import utilcalc.core.model.Payment;

/**
 * Represents a report section for deposit payments.
 *
 * <p>Contains a list of deposit {@link Payment}.
 *
 * <p>This class is immutable.
 */
public final class DepositSection extends ReportSection {

    private final List<Payment> payments;

    public DepositSection(String name, BigDecimal totalAmount, List<Payment> payments) {
        super(name, totalAmount);
        this.payments = List.copyOf(payments);
    }

    public List<Payment> getPayments() {
        return payments;
    }
}
