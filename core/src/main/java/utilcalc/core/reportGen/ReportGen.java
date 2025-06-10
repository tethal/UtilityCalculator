package utilcalc.core.reportGen;

import java.time.LocalDate;
import java.util.List;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.output.Report;

public final class ReportGen {

    private ReportGen() {}

    public static Report generateReport(ReportInputs reportInputs) {
        LocalDate startDate = reportInputs.startDate();
        LocalDate endDate = reportInputs.endDate();
        List<String> tenant = reportInputs.tenant();
        List<String> owner = reportInputs.owner();
        String reportPlace = reportInputs.reportPlace();
        LocalDate reportDate = reportInputs.reportDate();
        List<String> sources = reportInputs.sources();

        return new Report(
                startDate, endDate, tenant, owner, reportPlace, reportDate, sources, List.of());
    }
}
