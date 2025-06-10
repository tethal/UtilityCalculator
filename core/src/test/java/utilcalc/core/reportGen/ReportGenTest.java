package utilcalc.core.reportGen;

import static org.assertj.core.api.Assertions.assertThat;
import static utilcalc.core.reportGen.ReportGen.generateReport;

import org.junit.jupiter.api.Test;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.output.Report;

public class ReportGenTest {

    @Test
    void valid_ReportInput_should_return_valid_report_class() {
        ReportInputs reportInputs = ReportInputFactory.validReportInput();

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
        ReportInputs reportInputs = ReportInputFactory.emptySourceReportInput();

        Report report = generateReport(reportInputs);

        assertThat(report.sources()).isEmpty();
    }
}
