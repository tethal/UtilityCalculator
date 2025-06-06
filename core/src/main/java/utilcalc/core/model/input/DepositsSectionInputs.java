package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.ensureNonBlank;
import static utilcalc.core.utils.Util.ensureNonEmpty;

import java.util.List;

public record DepositsSectionInputs(String name, List<Payment> payments) implements SectionInputs {
    public DepositsSectionInputs {
        ensureNonBlank(name, "name");
        ensureNonEmpty(payments, "payments");
        payments = List.copyOf(payments);
    }
}
