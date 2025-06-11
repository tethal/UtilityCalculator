package utilcalc.core.reportGen;

import static utilcalc.core.utils.Util.ensureNotNegativeBigDecimalValue;

import java.math.BigDecimal;
import java.util.List;
import utilcalc.core.model.input.DepositsSectionInputs;
import utilcalc.core.model.input.Payment;
import utilcalc.core.model.output.Deposit;
import utilcalc.core.model.output.DepositSection;
import utilcalc.core.model.output.ReportSection;

public final class DepositSectionGenerator {

    public static ReportSection generateDepositSection(DepositsSectionInputs depositsInputs) {
        String name = depositsInputs.name();
        List<Payment> payments = depositsInputs.payments();
        List<Deposit> deposits = mapPaymentListToDepositList(payments);

        BigDecimal totalAmount =
                deposits.stream().map(Deposit::amount).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DepositSection(name, totalAmount, deposits);
    }

    private static Deposit mapPaymentToDeposit(Payment payment) {
        String description = payment.description();
        BigDecimal count = payment.count();
        BigDecimal unitAmount = payment.unitAmount();

        ensureNotNegativeBigDecimalValue(count, "deposit");
        ensureNotNegativeBigDecimalValue(unitAmount, "unitAmount");

        BigDecimal amount = calculateAmount(count, unitAmount);

        return new Deposit(description, count, unitAmount, amount);
    }

    private static List<Deposit> mapPaymentListToDepositList(List<Payment> payments) {
        return payments.stream().map(DepositSectionGenerator::mapPaymentToDeposit).toList();
    }

    private static BigDecimal calculateAmount(BigDecimal count, BigDecimal unitAmount) {
        return count.multiply(unitAmount).negate();
    }
}
