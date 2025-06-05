package utilcalc.core.model.output;

import java.math.BigDecimal;

public record HeatingPayment(
        String description,
        BigDecimal count,
        BigDecimal unitAmount,
        BigDecimal amount,
        BigDecimal coefficient)
        implements Payment {}
