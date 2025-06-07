package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.ensureNonBlank;
import static utilcalc.core.utils.Util.ensureNonNull;

import java.math.BigDecimal;

public record OtherFee(
        String description, BigDecimal annualCost, BigDecimal monthCount, BigDecimal feeAmount) {
    public OtherFee {
        ensureNonBlank(description, "description");
        ensureNonNull(annualCost, "annualCost");
        ensureNonNull(monthCount, "monthCount");
        ensureNonNull(feeAmount, "feeAmount");
    }
}
