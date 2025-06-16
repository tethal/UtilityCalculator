package utilcalc.core.reportGen;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static utilcalc.core.reportGen.OtherFeeSectionGenerator.generateOtherFeeSection;
import static utilcalc.core.reportGen.ServiceCostFactory.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.input.OtherFeeInputs;
import utilcalc.core.model.input.ServiceCost;
import utilcalc.core.model.output.OtherFee;
import utilcalc.core.model.output.OtherFeeSection;

class OtherFeeSectionGeneratorTest {
    private final ServiceCost validOneFullYearServiceCost = validOneFullYearServiceCost();

    @Test
    void otherFee_withOneServiceCost_should_haveCorrectNameAndSum() {
        OtherFeeSection otherFeeSection = createOtherFeeSection(validOneFullYearServiceCost);

        assertThat(otherFeeSection.name()).isEqualTo("Other fees");
        assertThat(otherFeeSection.totalAmount()).isEqualTo("8772.00");
    }

    @Test
    void otherFee_withOneServiceCost_should_haveCorrectOtherFeeProperties() {
        OtherFee otherFee = createOtherFeeSection(validOneFullYearServiceCost).fees().getFirst();

        assertThat(otherFee.description()).isEqualTo("1.1.2024 - 31.12.2024");
        assertThat(otherFee.annualCost()).isEqualTo("8772");
        assertThat(otherFee.monthCount()).isEqualTo("12.00");
        assertThat(otherFee.feeAmount()).isEqualTo("8772.00");
    }

    @Test
    void otherFee_withMultipleServiceCost_should_haveCorrectOtherFeeProperties() {
        OtherFeeSection otherFeeSection =
                createOtherFeeSection(
                        validOneFullYearServiceCost, validOneAndHalfYearServiceCost());

        OtherFee otherFee1 = otherFeeSection.fees().getFirst();
        OtherFee otherFee2 = otherFeeSection.fees().getLast();

        assertThat(otherFee1.description()).isEqualTo("1.1.2024 - 31.12.2024");
        assertThat(otherFee1.annualCost()).isEqualTo("8772");
        assertThat(otherFee1.monthCount()).isEqualTo("12.00");
        assertThat(otherFee1.feeAmount()).isEqualTo("8772.00");

        assertThat(otherFee2.description()).isEqualTo("1.1.2024 - 15.6.2025");
        assertThat(otherFee2.annualCost()).isEqualTo("8000");
        assertThat(otherFee2.monthCount()).isEqualTo("17.50");
        assertThat(otherFee2.feeAmount()).isEqualTo("11666.67");
    }

    @Test
    void otherFee_withPartialMonthServiceCost_should_haveCorrectOtherFeeProperties() {
        OtherFeeSection otherFeeSection = createOtherFeeSection(validPartialMonthServiceCost());

        OtherFee otherFee = otherFeeSection.fees().getFirst();

        assertThat(otherFeeSection.totalAmount()).isEqualTo("236.56");

        assertThat(otherFee.description()).isEqualTo("6.1.2024 - 16.1.2024");
        assertThat(otherFee.annualCost()).isEqualTo("8000");
        assertThat(otherFee.monthCount()).isEqualTo("0.35");
        assertThat(otherFee.feeAmount()).isEqualTo("236.56");
    }

    @Test
    void otherFee_withTwoPartialMonthServiceCost_should_haveCorrectOtherFeeProperties() {
        OtherFeeSection otherFeeSection = createOtherFeeSection(validTwoPartialMonthsServiceCost());

        OtherFee otherFee = otherFeeSection.fees().getFirst();

        assertThat(otherFee.description()).isEqualTo("6.12.2024 - 16.1.2025");
        assertThat(otherFee.annualCost()).isEqualTo("8000");
        assertThat(otherFee.monthCount()).isEqualTo("1.35");
        assertThat(otherFee.feeAmount()).isEqualTo("903.23");
    }

    private OtherFeeSection createOtherFeeSection(ServiceCost... serviceCosts) {
        return generateOtherFeeSection(new OtherFeeInputs("Other fees", List.of(serviceCosts)));
    }
}
