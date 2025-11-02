package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.ensureNonBlank;
import static utilcalc.core.utils.Util.ensureNonNull;

import java.math.BigDecimal;

public record CustomSectionInputs(String name, BigDecimal totalAmount) implements SectionInputs {
    public CustomSectionInputs {
        ensureNonBlank(name, "name");
        ensureNonNull(totalAmount, "totalAmount");
    }
}
