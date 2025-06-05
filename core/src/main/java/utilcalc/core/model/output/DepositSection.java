package utilcalc.core.model.output;

import java.math.BigDecimal;
import java.util.List;

public record DepositSection(String name, BigDecimal totalAmount, List<Payment> payments)
        implements ReportSection {
    public DepositSection {
        payments = List.copyOf(payments);
    }
}
