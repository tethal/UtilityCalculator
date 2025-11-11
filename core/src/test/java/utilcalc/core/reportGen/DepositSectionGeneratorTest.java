package utilcalc.core.reportGen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utilcalc.core.reportGen.DepositSectionGenerator.generateDepositSection;
import static utilcalc.core.reportGen.TestDataFactory.createDepositSectionInput;
import static utilcalc.core.reportGen.TestDataFactory.createPayment;

import org.junit.jupiter.api.Test;
import utilcalc.core.model.input.Payment;
import utilcalc.core.model.output.Deposit;
import utilcalc.core.model.output.DepositSection;

class DepositSectionGeneratorTest {

    @Test
    void depositSection_withOnePayment_should_haveCorrectNameAndSum() {
        DepositSection depositSection =
                generateDepositSection(createDepositSectionInput(createPayment("Leden - Červen", "6", "500")));

        assertThat(depositSection.name()).isEqualTo("Deposits");
        assertThat(depositSection.totalAmount()).isEqualTo("-3000");
    }

    @Test
    void depositSection_withOnePayment_should_haveCorrectDepositProperties() {
        Payment payment = createPayment("Leden - Červen", "6", "500");
        DepositSection depositSection = generateDepositSection(createDepositSectionInput(payment));
        Deposit deposit = depositSection.deposits().getFirst();

        assertDepositMatchesPayment(deposit, payment, "3000");
    }

    @Test
    void depositSection_withMultiplePayments_should_haveCorrectDepositsProperties() {
        Payment payment1 = createPayment("Leden - Červen", "6", "500");
        Payment payment2 = createPayment("Červenec - Září", "4", "600");
        DepositSection depositSection = generateDepositSection(createDepositSectionInput(payment1, payment2));
        Deposit deposit1 = depositSection.deposits().getFirst();
        Deposit deposit2 = depositSection.deposits().get(1);

        assertThat(depositSection.totalAmount()).isEqualTo("-5400");

        assertDepositMatchesPayment(deposit1, payment1, "3000");
        assertDepositMatchesPayment(deposit2, payment2, "2400");
    }

    @Test
    void payment_withInvalidCount_should_throw_illegalArgumentException() {
        assertThatThrownBy(() ->
                        generateDepositSection(createDepositSectionInput(createPayment("Leden - Červen", "-6", "200"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("count must not be a negative value");
    }

    private void assertDepositMatchesPayment(Deposit deposit, Payment payment, String expectedAmount) {

        assertThat(deposit.description()).isEqualTo(payment.description());
        assertThat(deposit.count()).isEqualTo(payment.count());
        assertThat(deposit.unitAmount()).isEqualTo(payment.unitAmount());
        assertThat(deposit.amount()).isEqualTo(expectedAmount);
    }
}
