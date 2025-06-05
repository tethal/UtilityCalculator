package utilcalc.core.model.output;

import java.math.BigDecimal;

public record BasicPayment(
        String description, BigDecimal count, BigDecimal unitAmount, BigDecimal amount)
        implements Payment {}
