package utilcalc.core.model.output;

import java.math.BigDecimal;
import java.util.List;

public record OtherFeeSection(String name, BigDecimal totalAmount, List<OtherFee> fees)
        implements ReportSection {
    public OtherFeeSection {
        fees = List.copyOf(fees);
    }
}
