package utilcalc.core.model.output;

import java.math.BigDecimal;
import java.util.List;

public record OtherFeeSection(String name, BigDecimal totalAmount, List<Payment> payments)
        implements ReportSection {
    public OtherFeeSection {
        payments = List.copyOf(payments);
    }
}
