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

    @Test
    void depositSection_withOnePayment_should_haveCorrectNameAndSum() {
        DepositSection depositSection = createDepositSection(payment1);

        assertThat(depositSection.name()).isEqualTo("deposits");
        assertThat(depositSection.totalAmount()).isEqualTo("-3000");
    }

    @Test
    void depositSection_withOnePayment_should_haveCorrectDepositProperties() {
        Deposit deposit = createDepositSection(payment1).deposits().getFirst();

        assertDepositMatchesPayment(deposit, payment1, "3000");
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
    void payment_withInvalidCount_should_throw_illegalArgumentException() {
        assertThatThrownBy(() -> createDepositSection(invalidPaymentWithNegativeCount()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("count must not be a negative value");
    }

    private void assertDepositMatchesPayment(
            Deposit deposit, Payment payment, String expectedAmount) {

        assertThat(deposit.description()).isEqualTo(payment.description());
        assertThat(deposit.count()).isEqualTo(payment.count());
        assertThat(deposit.unitAmount()).isEqualTo(payment.unitAmount());
        assertThat(deposit.amount()).isEqualTo(expectedAmount);
    }

    private DepositSection createDepositSection(Payment... payments) {
        return generateDepositSection(new DepositsSectionInputs("deposits", List.of(payments)));
    }
}
