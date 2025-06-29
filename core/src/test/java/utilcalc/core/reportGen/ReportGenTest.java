package utilcalc.core.reportGen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utilcalc.core.reportGen.ReportGen.generateReport;
import static utilcalc.core.reportGen.TestDataFactory.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.input.SectionInputs;
import utilcalc.core.model.input.ServiceCost;
import utilcalc.core.model.output.Report;

class ReportGenTest {

    @Test
    void valid_ReportInput_should_return_valid_report_class() {
        ReportInputs reportInputs = validReportInputWithSections();

        Report report = generateReport(reportInputs);

        assertThat(report.dateRange()).isEqualTo(reportInputs.dateRange());
        assertThat(report.tenant()).isEqualTo(reportInputs.tenant());
        assertThat(report.owner()).isEqualTo(reportInputs.owner());
        assertThat(report.reportPlace()).isEqualTo(reportInputs.reportPlace());
        assertThat(report.reportDate()).isEqualTo(reportInputs.reportDate());
        assertThat(report.sources()).isEqualTo(reportInputs.sources());
        assertThat(report.sections()).isEmpty();
    }

    @Test
    void empty_sources_should_be_empty_in_report_class() {
        ReportInputs reportInputs = emptySourceReportInput();

        Report report = generateReport(reportInputs);

        assertThat(report.sources()).isEmpty();
    }

    @Test
    void null_ReportInputs_should_throw_IllegalArgumentException() {
        assertThatThrownBy(() -> generateReport(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Report inputs must not be null");
    }

    @Test
    void invalid_ReportInputs_should_throw_IllegalStateException() {
        InvalidSectionInput invalidSectionInput =
                new InvalidSectionInput(
                        "Invalid section",
                        List.of(
                                createServiceCost(
                                        createDateRange("2024-01-01", "2025-01-01"), "8772")));

        ReportInputs reportInputs = validReportInputWithSections(invalidSectionInput);

        assertThatThrownBy(() -> generateReport(reportInputs))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unexpected section: " + invalidSectionInput.name());
    }

    @Test
    void valid_DepositSectionInputs_should_generate_valid_DepositSection() {
        ReportInputs reportInputs =
                validReportInputWithSections(
                        createDepositSectionInput(createPayment("Leden - Červen", "6", "500")));

        Report report = generateReport(reportInputs);

        assertThat(report.sections().getFirst().name()).isEqualTo("Deposits");
        assertThat(report.sections().getFirst().totalAmount()).isEqualTo("-3000");
    }

    @Test
    void valid_OtherFeeSectionInputs_should_generate_valid_OtherFeeSection() {
        ReportInputs reportInputs =
                validReportInputWithSections(
                        createOtherFeeInputs(
                                createServiceCost(
                                        createDateRange("2024-01-01", "2025-01-01"), "8772")));

        Report report = generateReport(reportInputs);

        assertThat(report.sections().getFirst().name()).isEqualTo("Other fees");
        assertThat(report.sections().getFirst().totalAmount()).isEqualTo("8772.00");
    }

    @Test
    void valid_HeatingFeeSectionInputs_should_generate_valid_HeatingFeeSection() {
        ReportInputs reportInputs =
                validReportInputWithSections(
                        createHeatingFeeInputs(
                                createServiceCost(
                                        createDateRange("2024-01-01", "2025-01-01"), "8772")));

        Report report = generateReport(reportInputs);

        assertThat(report.sections().getFirst().name()).isEqualTo("Heating fees");
        assertThat(report.sections().getFirst().totalAmount()).isEqualTo("8772.00");
    }

    private record InvalidSectionInput(String name, List<ServiceCost> serviceCosts)
            implements SectionInputs {}
}
