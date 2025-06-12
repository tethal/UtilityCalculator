package utilcalc.core.reportGen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utilcalc.core.reportGen.DepositSectionGenerator.generateDepositSection;
import static utilcalc.core.reportGen.PaymentFactory.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.input.DepositsSectionInputs;
import utilcalc.core.model.input.Payment;
import utilcalc.core.model.output.Deposit;
import utilcalc.core.model.output.DepositSection;

class DepositSectionGeneratorTest {
    private final Payment payment1 = validPayment1();
    private final Payment payment2 = validPayment2();
    private final DepositSection depositSectionWithOnePayment = createDepositSection(payment1);

    @Test
    void depositSection_withOnePayment_should_haveCorrectNameAndSum() {
        DepositSection depositSection = depositSectionWithOnePayment;

        assertThat(depositSection.name()).isEqualTo("deposits");
        assertThat(depositSection.totalAmount()).isEqualTo("-3000");
    }

    @Test
    void depositSection_withOnePayment_should_HaveCorrectDepositProperties() {
        Deposit deposit = depositSectionWithOnePayment.deposits().getFirst();

        assertDepositMatchesPayment(deposit, payment1, null);
    }

    @Test
    void depositSection_withMultiplePayments_should_haveCorrectDepositsProperties() {
        DepositSection depositSection = createDepositSection(payment1, payment2);
        Deposit deposit1 = depositSection.deposits().getFirst();
        Deposit deposit2 = depositSection.deposits().get(1);

        assertThat(depositSection.totalAmount()).isEqualTo("-5400");

        assertDepositMatchesPayment(deposit1, payment1, "3000");
        assertDepositMatchesPayment(deposit2, payment2, "2400");
    }

    @Test
    void payment_withInvalidValues_should_throw_illegalArgumentException() {
        DepositsSectionInputs depositsSectionInputs =
                new DepositsSectionInputs("deposits", List.of(invalidPaymentWithNegativeCount()));

        assertThatThrownBy(() -> generateDepositSection(depositsSectionInputs))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("deposit must not be a negative value");
    }

    private void assertDepositMatchesPayment(
            Deposit deposit, Payment payment, String expectedAmount) {

        assertThat(deposit.description()).isEqualTo(payment.description());
        assertThat(deposit.count()).isEqualTo(payment.count());
        assertThat(deposit.unitAmount()).isEqualTo(payment.unitAmount());

        if (expectedAmount != null) {
            assertThat(deposit.amount()).isEqualTo(expectedAmount);
        }
    }

    private DepositSection createDepositSection(Payment... payments) {
        return generateDepositSection(new DepositsSectionInputs("deposits", List.of(payments)));
    }
}
