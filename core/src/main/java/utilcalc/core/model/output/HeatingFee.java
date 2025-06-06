package utilcalc.core.model.output;

import java.math.BigDecimal;

public record HeatingFee(
        String description,
        BigDecimal annualCost,
        BigDecimal monthCount,
        BigDecimal coefficient,
        BigDecimal feeAmount) {}
