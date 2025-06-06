package utilcalc.core.model.output;

import java.math.BigDecimal;
import java.util.List;

public record DepositSection(String name, BigDecimal totalAmount, List<Deposit> deposits)
        implements ReportSection {
    public DepositSection {
        deposits = List.copyOf(deposits);
    }
}
