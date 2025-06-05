package utilcalc.core.model.input;

import static utilcalc.core.utils.Util.ensureNonEmpty;

import java.util.List;

public final class DepositsSectionInputs extends SectionInput {
    private final List<Payment> payments;

    public DepositsSectionInputs(String name, List<Payment> payments) {
        super(name);

        ensureNonEmpty(payments, "payments");

        this.payments = List.copyOf(payments);
    }

    public List<Payment> getPayments() {
        return payments;
    }
}
