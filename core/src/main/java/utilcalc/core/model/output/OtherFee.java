package utilcalc.core.model.output;

import java.math.BigDecimal;

public record OtherFee(
        String description, BigDecimal annualCost, BigDecimal monthCount, BigDecimal feeAmount) {}
