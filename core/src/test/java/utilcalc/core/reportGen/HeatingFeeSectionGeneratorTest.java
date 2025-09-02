package utilcalc.core.reportGen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utilcalc.core.reportGen.HeatingFeeSectionGenerator.calculateHeatingFee;
import static utilcalc.core.reportGen.HeatingFeeSectionGenerator.generateHeatingFeeSection;
import static utilcalc.core.reportGen.TestDataFactory.*;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.output.HeatingFee;
import utilcalc.core.model.output.HeatingFeeSection;

public class HeatingFeeSectionGeneratorTest {

    @Test
    void heatingFee_withOneServiceCost_should_haveCorrectNameAndSum() {
        HeatingFeeSection heatingFeeSection =
                generateHeatingFeeSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHeatingFeeInputs(
                                createServiceCost(
                                        createDateRange("2024-01-01", "2025-01-01"), "8772")));

        assertThat(heatingFeeSection.name()).isEqualTo("Heating fees");
        assertThat(heatingFeeSection.totalAmount()).isEqualTo("8772.00");
        assertThat(heatingFeeSection.fees()).hasSize(12);
    }

    @Test
    void heatingFee_withDecimalPlaces_should_haveCorrectSum() {
        HeatingFeeSection heatingFeeSection =
                generateHeatingFeeSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHeatingFeeInputs(
                                createServiceCost(
                                        createDateRange("2024-01-01", "2025-01-01"), "12431.84")));

        assertThat(heatingFeeSection.totalAmount()).isEqualTo("12431.84");
    }

    @Test
    void heatingFee_withOneServiceCost_should_haveCorrectHeatingFeeProperties() {
        HeatingFeeSection heatingFeeSection =
                generateHeatingFeeSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHeatingFeeInputs(
                                createServiceCost(
                                        createDateRange("2024-01-01", "2025-01-01"), "8772")));

        List<ExpectedFee> expectedFees =
                List.of(
                        new ExpectedFee("2024-01", "0.19", "1666.68", "8772"),
                        new ExpectedFee("2024-02", "0.16", "1403.52", "8772"),
                        new ExpectedFee("2024-03", "0.14", "1228.08", "8772"),
                        new ExpectedFee("2024-04", "0.09", "789.48", "8772"),
                        new ExpectedFee("2024-05", "0.02", "175.44", "8772"),
                        new ExpectedFee("2024-06", "0.0", "0.00", "8772"),
                        new ExpectedFee("2024-07", "0.0", "0.00", "8772"),
                        new ExpectedFee("2024-08", "0.0", "0.00", "8772"),
                        new ExpectedFee("2024-09", "0.01", "87.72", "8772"),
                        new ExpectedFee("2024-10", "0.08", "701.76", "8772"),
                        new ExpectedFee("2024-11", "0.14", "1228.08", "8772"),
                        new ExpectedFee("2024-12", "0.17", "1491.24", "8772"));

        List<HeatingFee> actualFees = heatingFeeSection.fees();

        assertThat(actualFees).hasSize(expectedFees.size());

        for (int i = 0; i < expectedFees.size(); i++) {
            HeatingFee actual = actualFees.get(i);
            ExpectedFee expected = expectedFees.get(i);

            assertThat(new BigDecimal(actual.monthCount().toString())).isEqualByComparingTo("1");
            assertThat(actual.yearMonth()).isEqualTo(expected.month());
            assertThat(actual.annualCost()).isEqualTo("8772");
            assertThat(actual.coefficient()).isEqualTo(expected.coefficient());
            assertThat(actual.feeAmount()).isEqualTo(expected.feeAmount());
        }
    }

    @Test
    void heatingFee_withMultipleServiceCost_should_haveCorrectHeatingFeeProperties() {
        DateRange heatingFee1DateRange = createDateRange("2024-01-01", "2025-01-01");
        DateRange heatingFee2DateRange = createDateRange("2025-01-01", "2026-01-01");

        HeatingFeeSection heatingFeeSection =
                generateHeatingFeeSection(
                        createDateRange("2024-06-01", "2025-06-01"),
                        createHeatingFeeInputs(
                                createServiceCost(heatingFee1DateRange, "8772"),
                                createServiceCost(heatingFee2DateRange, "8000")));

        assertThat(heatingFeeSection.name()).isEqualTo("Heating fees");
        assertThat(heatingFeeSection.totalAmount()).isEqualTo("8308.80");
        assertThat(heatingFeeSection.fees()).hasSize(12);

        List<ExpectedFee> expectedFees =
                List.of(
                        new ExpectedFee("2024-06", "0.0", "0.00", "8772"),
                        new ExpectedFee("2024-07", "0.0", "0.00", "8772"),
                        new ExpectedFee("2024-08", "0.0", "0.00", "8772"),
                        new ExpectedFee("2024-09", "0.01", "87.72", "8772"),
                        new ExpectedFee("2024-10", "0.08", "701.76", "8772"),
                        new ExpectedFee("2024-11", "0.14", "1228.08", "8772"),
                        new ExpectedFee("2024-12", "0.17", "1491.24", "8772"),
                        new ExpectedFee("2025-01", "0.19", "1520.00", "8000"),
                        new ExpectedFee("2025-02", "0.16", "1280.00", "8000"),
                        new ExpectedFee("2025-03", "0.14", "1120.00", "8000"),
                        new ExpectedFee("2025-04", "0.09", "720.00", "8000"),
                        new ExpectedFee("2025-05", "0.02", "160.00", "8000"));

        List<HeatingFee> actualFees = heatingFeeSection.fees();

        assertThat(actualFees).hasSize(expectedFees.size());

        for (int i = 0; i < expectedFees.size(); i++) {
            HeatingFee actual = actualFees.get(i);
            ExpectedFee expected = expectedFees.get(i);

            assertThat(new BigDecimal(actual.monthCount().toString())).isEqualByComparingTo("1");
            assertThat(actual.yearMonth()).isEqualTo(expected.month);
            assertThat(actual.annualCost()).isEqualTo(expected.annualCost);
            assertThat(actual.coefficient()).isEqualTo(expected.coefficient);
            assertThat(actual.feeAmount()).isEqualTo(expected.feeAmount);
        }
    }

    @Test
    void heatingFee_withTwoServiceCostsSplittingMonth_should_haveCorrectHeatingFeeProperties() {
        DateRange heatingFee1DateRange = createDateRange("2024-01-01", "2024-06-15");
        DateRange heatingFee2DateRange = createDateRange("2024-06-15", "2025-01-01");

        HeatingFeeSection heatingFeeSection =
                generateHeatingFeeSection(
                        createDateRange("2024-01-01", "2025-01-01"),
                        createHeatingFeeInputs(
                                createServiceCost(heatingFee1DateRange, "8772"),
                                createServiceCost(heatingFee2DateRange, "8000")));

        assertThat(heatingFeeSection.name()).isEqualTo("Heating fees");
        assertThat(heatingFeeSection.totalAmount()).isEqualTo("8463.20");
        assertThat(heatingFeeSection.fees()).hasSize(13);

        List<ExpectedFee> expectedFees =
                List.of(
                        new ExpectedFee("2024-01", "0.19", "1666.68", "8772"),
                        new ExpectedFee("2024-02", "0.16", "1403.52", "8772"),
                        new ExpectedFee("2024-03", "0.14", "1228.08", "8772"),
                        new ExpectedFee("2024-04", "0.09", "789.48", "8772"),
                        new ExpectedFee("2024-05", "0.02", "175.44", "8772"),
                        new ExpectedFee("2024-06", "0.0", "0.00", "8772"),
                        new ExpectedFee("2024-06", "0.0", "0.00", "8000"),
                        new ExpectedFee("2024-07", "0.0", "0.00", "8000"),
                        new ExpectedFee("2024-08", "0.0", "0.00", "8000"),
                        new ExpectedFee("2024-09", "0.01", "80.00", "8000"),
                        new ExpectedFee("2024-10", "0.08", "640.00", "8000"),
                        new ExpectedFee("2024-11", "0.14", "1120.00", "8000"),
                        new ExpectedFee("2024-12", "0.17", "1360.00", "8000"));

        List<HeatingFee> actualFees = heatingFeeSection.fees();

        assertThat(actualFees).hasSize(expectedFees.size());

        for (int i = 0; i < expectedFees.size(); i++) {
            HeatingFee actual = actualFees.get(i);
            ExpectedFee expected = expectedFees.get(i);

            assertThat(actualFees.get(5).monthCount()).isEqualTo("0.47");
            assertThat(actualFees.get(6).monthCount()).isEqualTo("0.53");
            assertThat(actual.yearMonth()).isEqualTo(expected.month);
            assertThat(actual.annualCost()).isEqualTo(expected.annualCost);
            assertThat(actual.coefficient()).isEqualTo(expected.coefficient);
            assertThat(actual.feeAmount()).isEqualTo(expected.feeAmount);
        }
    }

    @Test
    void heatingFee_withPartialMonthReportDateRange_should_haveCorrectHeatingFeeProperties() {
        DateRange heatingFeeSectionDateRange = createDateRange("2024-01-15", "2024-01-25");

        HeatingFeeSection heatingFeeSection =
                generateHeatingFeeSection(
                        heatingFeeSectionDateRange,
                        createHeatingFeeInputs(
                                createServiceCost(
                                        createDateRange("2024-01-01", "2025-01-01"), "8772")));

        HeatingFee heatingFee = heatingFeeSection.fees().getFirst();

        assertThat(heatingFeeSection.totalAmount()).isEqualTo("537.64");
        assertThat(heatingFeeSection.fees()).hasSize(1);

        assertThat(heatingFee.monthCount()).isEqualByComparingTo("0.32");
        assertThat(heatingFee.yearMonth()).isEqualTo("2024-01");
        assertThat(heatingFee.annualCost()).isEqualTo("8772");
        assertThat(heatingFee.coefficient()).isEqualTo("0.19");
        assertThat(heatingFee.feeAmount()).isEqualTo("537.64");
    }

    @Test
    void multipleMonthServiceCost_should_throw_illegalArgumentException() {
        assertThatThrownBy(
                        () ->
                                calculateHeatingFee(
                                        createServiceCost(
                                                createDateRange("2024-01-01", "2024-02-02"),
                                                "8000")))
                .hasMessage("Date range of service cost must be within a single month");
    }

    record ExpectedFee(String month, String coefficient, String feeAmount, String annualCost) {}
}
