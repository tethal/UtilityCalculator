package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.DepositSectionGenerator.generateDepositSection;
import static utilcalc.core.utils.Util.ensureNonNull;

import java.util.List;
import java.util.stream.Collectors;
import utilcalc.core.model.input.DepositsSectionInputs;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.input.SectionInputs;
import utilcalc.core.model.output.Report;
import utilcalc.core.model.output.ReportSection;

public final class ReportGen {

    private ReportGen() {}

    public static Report generateReport(ReportInputs reportInputs) {
        ensureNonNull(reportInputs, "Report inputs");

        List<SectionInputs> inputSections = reportInputs.sections();

        List<ReportSection> reportSections =
                inputSections.stream()
                        .map(ReportGen::generateReportSection)
                        .collect(Collectors.toList());

        return new Report(
                reportInputs.dateRange(),
                reportInputs.tenant(),
                reportInputs.owner(),
                reportInputs.reportPlace(),
                reportInputs.reportDate(),
                reportInputs.sources(),
                reportSections);
    }

    private static ReportSection generateReportSection(SectionInputs sectionInputs) {
        return switch (sectionInputs) {
            case DepositsSectionInputs deposit -> generateDepositSection(deposit);
            default -> throw new IllegalStateException(
                    "Unexpected section: " + sectionInputs.name());
        };
    }
}
