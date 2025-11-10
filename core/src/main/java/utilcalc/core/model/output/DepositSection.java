package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.*;

import java.math.BigDecimal;
import java.util.List;

public record DepositSection(String name, BigDecimal totalAmount, List<Deposit> deposits) implements ReportSection {
    public DepositSection {
        ensureNonBlank(name, "name");
        ensureNonNull(totalAmount, "totalAmount");
        ensureNonEmpty(deposits, "deposits");

        deposits = List.copyOf(deposits);
    }
}
