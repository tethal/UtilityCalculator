package utilcalc.core.model.output;

import java.math.BigDecimal;
import java.util.List;

public record HeatingFeeSection(String name, BigDecimal totalAmount, List<HeatingFee> fees)
        implements ReportSection {
    public HeatingFeeSection {
        fees = List.copyOf(fees);
    }
}
