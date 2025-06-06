package utilcalc.core.model.output;

import java.math.BigDecimal;

public record Deposit(
        String description, BigDecimal count, BigDecimal unitAmount, BigDecimal amount) {}
