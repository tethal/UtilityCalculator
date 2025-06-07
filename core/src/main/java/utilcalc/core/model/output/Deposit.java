package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.ensureNonBlank;
import static utilcalc.core.utils.Util.ensureNonNull;

import java.math.BigDecimal;

public record Deposit(
        String description, BigDecimal count, BigDecimal unitAmount, BigDecimal amount) {
    public Deposit {
        ensureNonBlank(description, "description");
        ensureNonNull(count, "count");
        ensureNonNull(unitAmount, "unitAmount");
        ensureNonNull(amount, "amount");
    }
}
