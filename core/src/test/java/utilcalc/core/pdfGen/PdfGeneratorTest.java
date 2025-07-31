package utilcalc.core.pdfGen;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.*;
import utilcalc.core.model.output.Report;
import utilcalc.core.reportGen.ReportGen;

class PdfGeneratorTest {

    @Test
    void generatePdf_should_return_non_empty_bytes_and_write_file() throws IOException {
        SectionInputs deposits =
                new DepositsSectionInputs(
                        "Přijaté zálohy",
                        List.of(
                                new Payment(
                                        "Záloha na vodu",
                                        BigDecimal.valueOf(3),
                                        BigDecimal.valueOf(180)),
                                new Payment(
                                        "Záloha na elektřinu",
                                        BigDecimal.valueOf(2),
                                        BigDecimal.valueOf(250))));

        DateRange dateRange =
                DateRange.fromInclusive(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

        ReportInputs inputs =
                new ReportInputs(
                        dateRange,
                        List.of("Jan Nájemník"),
                        List.of("Karel Vlastník"),
                        "Praha",
                        LocalDate.now(),
                        List.of("Faktury, podklady měření"),
                        List.of(deposits));

        Report report = ReportGen.generateReport(inputs);

        byte[] pdfBytes = PdfGenerator.generatePdf(report);

        assertNotNull(pdfBytes, "PDF nebyl vygenerován.");
        assertTrue(pdfBytes.length > 100, "Vygenerované PDF je příliš malé – nemusí být validní.");

        File outputFile = new File("build/test-output/test-report.pdf");
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                throw new IOException(
                        "Nepodařilo se vytvořit adresář: " + parentDir.getAbsolutePath());
            }
        }
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            out.write(pdfBytes);
        }

        assertTrue(outputFile.exists(), "Výstupní PDF soubor nebyl vytvořen.");
    }
}
