package utilcalc.core.model.output;

import static utilcalc.core.utils.Util.ensureNonBlank;
import static utilcalc.core.utils.Util.ensureNonNull;

import java.math.BigDecimal;

public record CustomSection(String name, BigDecimal totalAmount) implements ReportSection {

    public CustomSection {
        ensureNonBlank(name, "name");
        ensureNonNull(totalAmount, "totalAmount");
    }
}
