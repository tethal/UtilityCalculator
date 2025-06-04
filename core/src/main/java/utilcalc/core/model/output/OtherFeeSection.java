package utilcalc.core.model.output;

import java.math.BigDecimal;
import java.util.List;
import utilcalc.core.model.Payment;

/**
 * Represent a report section for other fee.
 *
 * <p>Contains a list of other {@link Payment}.
 *
 * <p>This class is immutable.
 */
public final class OtherFeeSection extends ReportSection {

    private final List<Payment> otherFees;

    public OtherFeeSection(String name, BigDecimal totalAmount, List<Payment> otherFees) {
        super(name, totalAmount);
        this.otherFees = List.copyOf(otherFees);
    }

    public List<Payment> getOtherFees() {
        return otherFees;
    }

}
