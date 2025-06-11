package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.DepositSectionGenerator.generateDepositSection;
import static utilcalc.core.utils.Util.ensureNonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utilcalc.core.model.input.DepositsSectionInputs;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.input.SectionInputs;
import utilcalc.core.model.output.Report;
import utilcalc.core.model.output.ReportSection;

public final class ReportGen {

    private ReportGen() {}

    public static Report generateReport(ReportInputs reportInputs) {
        ensureNonNull(reportInputs, "Report inputs");

        LocalDate startDate = reportInputs.startDate();
        LocalDate endDate = reportInputs.endDate();
        List<String> tenant = reportInputs.tenant();
        List<String> owner = reportInputs.owner();
        String reportPlace = reportInputs.reportPlace();
        LocalDate reportDate = reportInputs.reportDate();
        List<String> sources = reportInputs.sources();
        List<SectionInputs> inputSections = reportInputs.sections();

        List<ReportSection> reportSections = new ArrayList<>();
        generateReportSection(reportSections, inputSections);

        return new Report(
                startDate,
                endDate,
                tenant,
                owner,
                reportPlace,
                reportDate,
                sources,
                reportSections);
    }

    private static void generateReportSection(
            List<ReportSection> reportSections, List<SectionInputs> sections) {

        for (SectionInputs sectionInputs : sections) {

            switch (sectionInputs) {
                case DepositsSectionInputs deposit -> reportSections.add(
                        generateDepositSection(deposit));
                default -> throw new IllegalStateException(
                        "Unexpected section: " + sectionInputs.name());
            }
        }
    }
}
