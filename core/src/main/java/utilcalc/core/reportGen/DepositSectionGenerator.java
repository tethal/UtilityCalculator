package utilcalc.core.reportGen;

import static utilcalc.core.utils.Util.ensureNotNegativeBigDecimalValue;

import java.math.BigDecimal;
import java.util.List;
import utilcalc.core.model.input.DepositsSectionInputs;
import utilcalc.core.model.input.Payment;
import utilcalc.core.model.output.Deposit;
import utilcalc.core.model.output.DepositSection;

final class DepositSectionGenerator {

    private DepositSectionGenerator() {}

    static DepositSection generateDepositSection(DepositsSectionInputs depositsInputs) {
        String name = depositsInputs.name();
        List<Payment> payments = depositsInputs.payments();

        List<Deposit> deposits =
                payments.stream().map(DepositSectionGenerator::mapPaymentToDeposit).toList();

        BigDecimal totalAmount =
                deposits.stream()
                        .map(Deposit::amount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .negate();

        return new DepositSection(name, totalAmount, deposits);
    }

    private static Deposit mapPaymentToDeposit(Payment payment) {
        String description = payment.description();
        BigDecimal count = payment.count();
        BigDecimal unitAmount = payment.unitAmount();

        ensureNotNegativeBigDecimalValue(count, "deposit");

        BigDecimal amount = count.multiply(unitAmount);

        return new Deposit(description, count, unitAmount, amount);
    }
}
