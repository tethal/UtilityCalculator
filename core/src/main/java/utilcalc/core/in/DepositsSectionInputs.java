package utilcalc.core.in;

import java.math.BigDecimal;
import java.util.List;

public final class DepositsSectionInputs extends SectionInput {
    private final List<Payment> payments;

    public DepositsSectionInputs(String name, List<Payment> payments) {
        super(name);

        if (payments == null || payments.isEmpty()) {
            throw new IllegalArgumentException("payments is null or empty");
        }

        this.payments = List.copyOf(payments);
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public boolean allCountsAreOne() {
        return payments.stream().allMatch(p -> BigDecimal.ONE.equals(p.getCount()));
    }

    public BigDecimal getTotalDeposits() {
        return payments.stream()
                .map(Payment::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
