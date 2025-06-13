package utilcalc.core.reportGen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utilcalc.core.reportGen.ReportGen.generateReport;
import static utilcalc.core.reportGen.ReportInputFactory.*;

import org.junit.jupiter.api.Test;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.output.Report;

class ReportGenTest {

    @Test
    void valid_ReportInput_should_return_valid_report_class() {
        ReportInputs reportInputs = validReportInputWithEmptySections();

        Report report = generateReport(reportInputs);

        assertThat(report.startDate()).isEqualTo(reportInputs.startDate());
        assertThat(report.endDate()).isEqualTo(reportInputs.endDate());
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
    void valid_DepositSectionInputs_should_generate_valid_DepositSection() {
        ReportInputs reportInputs = validReportInputWithDepositSection();

        Report report = generateReport(reportInputs);

        assertThat(report.sections().getFirst().name()).isEqualTo("deposits");
        assertThat(report.sections().getFirst().totalAmount()).isEqualTo("-3000");
    }
}
