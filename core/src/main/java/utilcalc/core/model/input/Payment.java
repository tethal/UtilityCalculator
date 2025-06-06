package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.*;

import java.math.BigDecimal;

public record Payment(String description, BigDecimal count, BigDecimal unitAmount) {
    public Payment {
        ensureNonBlank(description, "description");
        ensureNonNull(count, "count");
        ensureNonNull(unitAmount, "unitAmount");
    }
}
