package utilcalc.core.reportGen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utilcalc.core.reportGen.DepositSectionGenerator.generateDepositSection;
import static utilcalc.core.reportGen.PaymentFactory.*;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utilcalc.core.model.input.DepositsSectionInputs;
import utilcalc.core.model.input.Payment;
import utilcalc.core.model.output.DepositSection;

public class DepositSectionGeneratorTest {

    @Test
    void depositSection_withOnePayment_should_haveCorrectNameAndSum() {

        DepositsSectionInputs depositsSectionInputs =
                new DepositsSectionInputs("deposits", List.of(validPayment1()));

        DepositSection depositSection =
                (DepositSection) generateDepositSection(depositsSectionInputs);

        assertThat(depositSection.name()).isEqualTo("deposits");
        assertThat(depositSection.totalAmount()).isEqualTo("-3000");
    }

    @Test
    void depositSection_withMultiplePayments_should_haveCorrectNameAndSum() {

        DepositsSectionInputs depositsSectionInputs =
                new DepositsSectionInputs("deposits", List.of(validPayment1(), validPayment2()));

        DepositSection depositSection =
                (DepositSection) generateDepositSection(depositsSectionInputs);

        assertThat(depositSection.name()).isEqualTo("deposits");
        assertThat(depositSection.totalAmount()).isEqualTo("-5400");
    }

    @ParameterizedTest
    @MethodSource("invalidPaymentsProvider")
    void payment_withInvalidValues_should_throw_illegalArgumentException(
            Payment invalidPayment, String expectedMessage) {
        DepositsSectionInputs depositsSectionInputs =
                new DepositsSectionInputs("deposit", List.of(invalidPayment));

        assertThatThrownBy(() -> generateDepositSection(depositsSectionInputs))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }

    private static Stream<Arguments> invalidPaymentsProvider() {
        return Stream.of(
                Arguments.of(
                        invalidPaymentWithNegativeCount(), "deposit must not be a negative value"),
                Arguments.of(
                        invalidPaymentWithNegativeUnitAmount(),
                        "unitAmount must not be a negative value"));
    }
}
