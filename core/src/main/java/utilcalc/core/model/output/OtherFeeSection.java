package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.*;

import java.math.BigDecimal;
import java.util.List;

public record OtherFeeSection(String name, BigDecimal totalAmount, List<OtherFee> fees) implements ReportSection {
    public OtherFeeSection {
        ensureNonBlank(name, "name");
        ensureNonNull(totalAmount, "totalAmount");
        ensureNonEmpty(fees, "fees");

        fees = List.copyOf(fees);
    }
}
