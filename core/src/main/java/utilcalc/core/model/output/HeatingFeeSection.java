package utilcalc.core.model.output;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represent a report section for heating fee.
 *
 * <p>Contains a list of {@link HeatingPayment}.
 *
 * <p>This class is immutable.
 */
public final class HeatingFeeSection extends ReportSection {

    private final List<HeatingPayment> heatingFees;

    public HeatingFeeSection(
            String name, BigDecimal totalAmount, List<HeatingPayment> heatingFees) {
        super(name, totalAmount);
        this.heatingFees = List.copyOf(heatingFees);
    }

    public List<HeatingPayment> getHeatingFees() {
        return heatingFees;
    }
}
