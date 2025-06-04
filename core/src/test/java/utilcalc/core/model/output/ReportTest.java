package utilcalc.core.model.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ReportTest {

    @Test
    void testReportFields() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        List<String> tenant = List.of("Jméno Nájemníka", "Adresa nemovitosti");
        List<String> owner = List.of("Jméno Majitele", "Kontakt");
        String reportPlace = "Praha";
        LocalDate reportDate = LocalDate.of(2025, 3, 15);
        List<String> sources = List.of("Zpráva SVJ");

        DepositSection depositSection = TestOutputDataFactory.createDepositSection();
        List<ReportSection> sections = List.of(depositSection);

        Report report =
                new Report(
                        startDate,
                        endDate,
                        tenant,
                        owner,
                        reportPlace,
                        reportDate,
                        sources,
                        sections);

        assertEquals(startDate, report.getStartDate());
        assertEquals(endDate, report.getEndDate());
        assertEquals(tenant, report.getTenant());
        assertEquals(owner, report.getOwner());
        assertEquals(reportPlace, report.getReportPlace());
        assertEquals(reportDate, report.getReportDate());
        assertEquals(sources, report.getSources());
        assertEquals(sections, report.getSections());
    }
}
