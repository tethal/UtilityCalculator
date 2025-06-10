package utilcalc.core.reportGen;

import java.time.LocalDate;
import java.util.List;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.input.SectionInputs;

final class ReportInputFactory {

    private static final LocalDate START_DATE = LocalDate.of(2024, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2024, 12, 31);
    private static final List<String> TENANT = List.of("Jméno nájemníka", "Adresa nemovitosti");
    private static final List<String> OWNER = List.of("Jméno majitele", "majitel@example.com");
    private static final String REPORT_PLACE = "V Praze";
    private static final LocalDate REPORT_DATE = LocalDate.of(2025, 2, 15);
    private static final List<String> SOURCES = List.of("Vyúčtování SVJ");
    private static final List<String> EMPTY_SOURCES = List.of();
    private static final List<SectionInputs> SECTIONS = List.of();

    private ReportInputFactory() {}

    public static ReportInputs validReportInput() {
        return new ReportInputs(
                START_DATE, END_DATE, TENANT, OWNER, REPORT_PLACE, REPORT_DATE, SOURCES, SECTIONS);
    }

    public static ReportInputs emptySourceReportInput() {
        return new ReportInputs(
                START_DATE,
                END_DATE,
                TENANT,
                OWNER,
                REPORT_PLACE,
                REPORT_DATE,
                EMPTY_SOURCES,
                SECTIONS);
    }
}
