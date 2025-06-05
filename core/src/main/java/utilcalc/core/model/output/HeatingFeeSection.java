package utilcalc.core.model.output;

import java.math.BigDecimal;
import java.util.List;

public record HeatingFeeSection(String name, BigDecimal totalAmount, List<HeatingPayment> payments)
        implements ReportSection {
    public HeatingFeeSection {
        payments = List.copyOf(payments);
    }
}
