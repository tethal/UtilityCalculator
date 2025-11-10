package utilcalc.core.reportGen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utilcalc.core.reportGen.OtherFeeSectionGenerator.generateOtherFeeSection;
import static utilcalc.core.reportGen.TestDataFactory.*;

import org.junit.jupiter.api.Test;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.OtherFeeInputs;
import utilcalc.core.model.output.OtherFee;
import utilcalc.core.model.output.OtherFeeSection;

class OtherFeeSectionGeneratorTest {

    @Test
    void otherFee_withOneServiceCost_should_haveCorrectNameAndSum() {
        OtherFeeSection otherFeeSection = generateOtherFeeSection(
                createDateRange("2024-01-01", "2025-01-01"),
                createOtherFeeInputs(createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8772")));

        assertThat(otherFeeSection.name()).isEqualTo("Other fees");
        assertThat(otherFeeSection.totalAmount()).isEqualTo("8772.00");
        assertThat(otherFeeSection.fees()).hasSize(1);
    }

    @Test
    void otherFee_withOneServiceCost_should_haveCorrectOtherFeeProperties() {
        DateRange otherFeeDateRange = createDateRange("2024-01-01", "2025-01-01");

        OtherFeeSection otherFeeSection = generateOtherFeeSection(
                otherFeeDateRange,
                createOtherFeeInputs(createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8772")));

        OtherFee otherFee = otherFeeSection.fees().getFirst();

        assertThat(otherFee.dateRange()).isEqualTo(otherFeeDateRange);
        assertThat(otherFee.monthlyCost()).isEqualTo("731.00");
        assertThat(otherFee.monthCount()).isEqualTo("12.00");
        assertThat(otherFee.feeAmount()).isEqualTo("8772.00");
    }

    @Test
    void otherFee_withMultipleServiceCost_should_haveCorrectOtherFeeProperties() {
        DateRange otherFee1DateRange = createDateRange("2024-01-01", "2025-01-01");
        DateRange otherFee2DateRange = createDateRange("2025-01-01", "2026-01-01");

        OtherFeeSection otherFeeSection = generateOtherFeeSection(
                createDateRange("2024-01-01", "2026-01-01"),
                createOtherFeeInputs(
                        createServiceCost(otherFee1DateRange, "8772"), createServiceCost(otherFee2DateRange, "8000")));

        assertThat(otherFeeSection.totalAmount()).isEqualTo("16772.00");

        OtherFee otherFee1 = otherFeeSection.fees().getFirst();
        OtherFee otherFee2 = otherFeeSection.fees().getLast();

        assertThat(otherFee1.dateRange()).isEqualTo(otherFee1DateRange);
        assertThat(otherFee1.monthlyCost()).isEqualTo("731.00");
        assertThat(otherFee1.monthCount()).isEqualTo("12.00");
        assertThat(otherFee1.feeAmount()).isEqualTo("8772.00");

        assertThat(otherFee2.dateRange()).isEqualTo(otherFee2DateRange);
        assertThat(otherFee2.monthlyCost()).isEqualTo("666.67");
        assertThat(otherFee2.monthCount()).isEqualTo("12.00");
        assertThat(otherFee2.feeAmount()).isEqualTo("8000.00");
    }

    @Test
    void otherFee_withPartialMonthServiceCost_should_haveCorrectOtherFeeProperties() {
        DateRange otherFeeSectionDateRange = createDateRange("2024-01-15", "2024-01-25");

        OtherFeeSection otherFeeSection = generateOtherFeeSection(
                otherFeeSectionDateRange,
                createOtherFeeInputs(createServiceCost(createDateRange("2024-01-01", "2025-01-01"), "8772")));

        OtherFee otherFee = otherFeeSection.fees().getFirst();

        assertThat(otherFeeSection.totalAmount()).isEqualTo("235.81");

        assertThat(otherFee.dateRange()).isEqualTo(otherFeeSectionDateRange);
        assertThat(otherFee.monthlyCost()).isEqualTo("731.00");
        assertThat(otherFee.monthCount()).isEqualTo("0.32");
        assertThat(otherFee.feeAmount()).isEqualTo("235.81");
    }

    @Test
    void otherFee_withTwoPartialMonthServiceCost_should_haveCorrectOtherFeeProperties() {
        DateRange otherFee1DateRange = createDateRange("2024-01-01", "2025-01-01");
        DateRange otherFee2DateRange = createDateRange("2025-01-01", "2026-01-01");

        OtherFeeSection otherFeeSection = generateOtherFeeSection(
                createDateRange("2024-01-15", "2025-01-15"),
                createOtherFeeInputs(
                        createServiceCost(otherFee1DateRange, "8772"), createServiceCost(otherFee2DateRange, "8000")));

        assertThat(otherFeeSection.totalAmount()).isEqualTo("8742.95");

        OtherFee otherFee1 = otherFeeSection.fees().getFirst();
        OtherFee otherFee2 = otherFeeSection.fees().getLast();

        assertThat(otherFee1.dateRange()).isEqualTo(createDateRange("2024-01-15", "2025-01-01"));
        assertThat(otherFee1.monthlyCost()).isEqualTo("731.00");
        assertThat(otherFee1.monthCount()).isEqualTo("11.55");
        assertThat(otherFee1.feeAmount()).isEqualTo("8441.87");

        assertThat(otherFee2.dateRange()).isEqualTo(createDateRange("2025-01-01", "2025-01-15"));
        assertThat(otherFee2.monthlyCost()).isEqualTo("666.67");
        assertThat(otherFee2.monthCount()).isEqualTo("0.45");
        assertThat(otherFee2.feeAmount()).isEqualTo("301.08");
    }

    @Test
    void otherFee_withPartialServiceCost_should_throw_illegalArgumentException() {
        OtherFeeInputs otherFeeInputs =
                createOtherFeeInputs(createServiceCost(createDateRange("2024-01-15", "2025-01-15"), "8772"));

        assertThatThrownBy(() -> generateOtherFeeSection(createDateRange("2024-01-01", "2025-01-01"), otherFeeInputs))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ServiceCosts do not fully cover the report date interval.");
    }

    @Test
    void otherFee_withOverlapServiceCost_should_throw_illegalArgumentException() {
        OtherFeeInputs otherFeeInputs = createOtherFeeInputs(
                createServiceCost(createDateRange("2024-01-15", "2025-01-15"), "8775"),
                createServiceCost(createDateRange("2025-01-01", "2026-01-01"), "8000"));

        assertThatThrownBy(() -> generateOtherFeeSection(createDateRange("2025-01-01", "2026-01-01"), otherFeeInputs))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "ServiceCosts do not connect seamlessly or they overlap: "
                                + "ServiceCost[dateRange=DateRange[startDate=2024-01-15, endDateExclusive=2025-01-15], annualCost=8775] and "
                                + "ServiceCost[dateRange=DateRange[startDate=2025-01-01, endDateExclusive=2026-01-01], annualCost=8000]");
    }

    @Test
    void otherFee_withNotConnectServiceCost_should_throw_illegalArgumentException() {
        OtherFeeInputs otherFeeInputs = createOtherFeeInputs(
                createServiceCost(createDateRange("2024-01-01", "2024-12-15"), "8775"),
                createServiceCost(createDateRange("2025-01-01", "2026-01-01"), "8000"));

        assertThatThrownBy(() -> generateOtherFeeSection(createDateRange("2025-01-01", "2026-01-01"), otherFeeInputs))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "ServiceCosts do not connect seamlessly or they overlap: "
                                + "ServiceCost[dateRange=DateRange[startDate=2024-01-01, endDateExclusive=2024-12-15], annualCost=8775] and "
                                + "ServiceCost[dateRange=DateRange[startDate=2025-01-01, endDateExclusive=2026-01-01], annualCost=8000]");
    }
}
