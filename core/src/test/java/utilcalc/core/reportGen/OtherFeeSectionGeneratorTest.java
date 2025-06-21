package utilcalc.core.reportGen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utilcalc.core.reportGen.OtherFeeSectionFactory.*;

import org.junit.jupiter.api.Test;
import utilcalc.core.model.output.OtherFee;
import utilcalc.core.model.output.OtherFeeSection;

class OtherFeeSectionGeneratorTest {
    private static final OtherFeeSection YEAR_OTHER_FEE_SECTION = createOneYearOtherFeeSection();

    @Test
    void otherFee_withOneServiceCost_should_haveCorrectNameAndSum() {
        OtherFeeSection otherFeeSection = YEAR_OTHER_FEE_SECTION;

        assertThat(otherFeeSection.name()).isEqualTo("Other fees");
        assertThat(otherFeeSection.totalAmount()).isEqualTo("8772.00");
        assertThat(otherFeeSection.fees()).hasSize(1);
    }

    @Test
    void otherFee_withOneServiceCost_should_haveCorrectOtherFeeProperties() {
        OtherFee otherFee = YEAR_OTHER_FEE_SECTION.fees().getFirst();

        assertThat(otherFee.description()).isEqualTo("1.1.2024 - 31.12.2024");
        assertThat(otherFee.annualCost()).isEqualTo("8772");
        assertThat(otherFee.monthCount()).isEqualTo("12.00");
        assertThat(otherFee.feeAmount()).isEqualTo("8772.00");
    }

    @Test
    void otherFee_withMultipleServiceCost_should_haveCorrectOtherFeeProperties() {
        OtherFeeSection otherFeeSection = createTwoYearOtherFeeSection();

        assertThat(otherFeeSection.totalAmount()).isEqualTo("16772.00");

        OtherFee otherFee1 = otherFeeSection.fees().getFirst();
        OtherFee otherFee2 = otherFeeSection.fees().getLast();

        assertThat(otherFee1.description()).isEqualTo("1.1.2024 - 31.12.2024");
        assertThat(otherFee1.annualCost()).isEqualTo("8772");
        assertThat(otherFee1.monthCount()).isEqualTo("12.00");
        assertThat(otherFee1.feeAmount()).isEqualTo("8772.00");

        assertThat(otherFee2.description()).isEqualTo("1.1.2025 - 31.12.2025");
        assertThat(otherFee2.annualCost()).isEqualTo("8000");
        assertThat(otherFee2.monthCount()).isEqualTo("12.00");
        assertThat(otherFee2.feeAmount()).isEqualTo("8000.00");
    }

    @Test
    void otherFee_withPartialMonthServiceCost_should_haveCorrectOtherFeeProperties() {
        OtherFeeSection otherFeeSection = createPartialMonthOtherFeeSection();

        OtherFee otherFee = otherFeeSection.fees().getFirst();

        assertThat(otherFeeSection.totalAmount()).isEqualTo("235.81");

        assertThat(otherFee.description()).isEqualTo("15.1.2024 - 24.1.2024");
        assertThat(otherFee.annualCost()).isEqualTo("8772");
        assertThat(otherFee.monthCount()).isEqualTo("0.32");
        assertThat(otherFee.feeAmount()).isEqualTo("235.81");
    }

    @Test
    void otherFee_withTwoPartialMonthServiceCost_should_haveCorrectOtherFeeProperties() {
        OtherFeeSection otherFeeSection = createPartialMonthOverTwoYearsOtherFeeSection();

        assertThat(otherFeeSection.totalAmount()).isEqualTo("8742.95");

        OtherFee otherFee1 = otherFeeSection.fees().getFirst();
        OtherFee otherFee2 = otherFeeSection.fees().getLast();

        assertThat(otherFee1.description()).isEqualTo("15.1.2024 - 31.12.2024");
        assertThat(otherFee1.annualCost()).isEqualTo("8772");
        assertThat(otherFee1.monthCount()).isEqualTo("11.55");
        assertThat(otherFee1.feeAmount()).isEqualTo("8441.87");

        assertThat(otherFee2.description()).isEqualTo("1.1.2025 - 14.1.2025");
        assertThat(otherFee2.annualCost()).isEqualTo("8000");
        assertThat(otherFee2.monthCount()).isEqualTo("0.45");
        assertThat(otherFee2.feeAmount()).isEqualTo("301.08");
    }

    @Test
    void otherFee_withPartialServiceCost_should_throw_illegalArgumentException() {
        assertThatThrownBy(OtherFeeSectionFactory::invalidServiceCostCoverageOtherFeeSection)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ServiceCosts do not fully cover the report date interval.");
    }

    @Test
    void otherFee_withOverlapServiceCost_should_throw_illegalArgumentException() {
        assertThatThrownBy(OtherFeeSectionFactory::overlapServiceCostOtherFeeSection)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ServiceCosts do not connect seamlessly or they overlap: ");
    }
}
