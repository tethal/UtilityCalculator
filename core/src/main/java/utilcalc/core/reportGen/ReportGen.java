package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.DepositSectionGenerator.generateDepositSection;
import static utilcalc.core.reportGen.HeatingFeeSectionGenerator.generateHeatingFeeSection;
import static utilcalc.core.reportGen.OtherFeeSectionGenerator.generateOtherFeeSection;
import static utilcalc.core.utils.Util.ensureNonNull;

import java.util.List;
import java.util.stream.Collectors;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.*;
import utilcalc.core.model.output.Report;
import utilcalc.core.model.output.ReportSection;

public final class ReportGen {

    private ReportGen() {}

    public static Report generateReport(ReportInputs reportInputs) {
        ensureNonNull(reportInputs, "Report inputs");

        DateRange reportDateRange = reportInputs.dateRange();
        List<SectionInputs> inputSections = reportInputs.sections();

        List<ReportSection> reportSections =
                inputSections.stream()
                        .map(sectionInputs -> generateReportSection(sectionInputs, reportDateRange))
                        .collect(Collectors.toList());

        return new Report(
                reportDateRange,
                reportInputs.tenant(),
                reportInputs.owner(),
                reportInputs.reportPlace(),
                reportInputs.reportDate(),
                reportInputs.sources(),
                reportSections);
    }

    private static ReportSection generateReportSection(
            SectionInputs sectionInputs, DateRange reportDateRange) {
        return switch (sectionInputs) {
            case DepositsSectionInputs deposit -> generateDepositSection(deposit);
            case OtherFeeInputs otherFee -> generateOtherFeeSection(reportDateRange, otherFee);
            case HeatingFeeInputs heatingFee -> generateHeatingFeeSection(
                    reportDateRange, heatingFee);
            default -> throw new IllegalStateException(
                    "Unexpected section: " + sectionInputs.name());
        };
    }
}
