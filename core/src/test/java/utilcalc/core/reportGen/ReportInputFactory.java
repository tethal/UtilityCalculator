package utilcalc.core.reportGen;

import static utilcalc.core.reportGen.PaymentFactory.validPayment1;

import java.time.LocalDate;
import java.util.List;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.DepositsSectionInputs;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.input.SectionInputs;

final class ReportInputFactory {

    private static final LocalDate START_DATE = LocalDate.of(2024, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2024, 12, 31);

    private static final DateRange DATE_RANGE = new DateRange(START_DATE, END_DATE);

    private static final List<String> TENANT = List.of("Jméno nájemníka", "Adresa nemovitosti");
    private static final List<String> OWNER = List.of("Jméno majitele", "majitel@example.com");

    private static final String REPORT_PLACE = "V Praze";
    private static final LocalDate REPORT_DATE = LocalDate.of(2025, 2, 15);

    private static final List<String> EMPTY_SOURCES = List.of();
    private static final List<String> SOURCES = List.of("Vyúčtování SVJ");

    private static final List<SectionInputs> EMPTY_SECTIONS = List.of();
    private static final List<SectionInputs> DEPOSIT_SECTIONS =
            List.of(new DepositsSectionInputs("deposits", List.of(validPayment1())));

    private ReportInputFactory() {}

    public static ReportInputs validReportInputWithEmptySections() {
        return new ReportInputs(
                DATE_RANGE, TENANT, OWNER, REPORT_PLACE, REPORT_DATE, SOURCES, EMPTY_SECTIONS);
    }

    public static ReportInputs validReportInputWithDepositSection() {
        return new ReportInputs(
                DATE_RANGE, TENANT, OWNER, REPORT_PLACE, REPORT_DATE, SOURCES, DEPOSIT_SECTIONS);
    }

    public static ReportInputs emptySourceReportInput() {
        return new ReportInputs(
                DATE_RANGE,
                TENANT,
                OWNER,
                REPORT_PLACE,
                REPORT_DATE,
                EMPTY_SOURCES,
                EMPTY_SECTIONS);
    }
}
