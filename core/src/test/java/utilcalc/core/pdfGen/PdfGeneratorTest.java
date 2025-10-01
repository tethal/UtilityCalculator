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
                                        "leden - duben",
                                        BigDecimal.valueOf(4),
                                        BigDecimal.valueOf(3000)),
                                new Payment("květen", BigDecimal.ONE, BigDecimal.valueOf(3500)),
                                new Payment(
                                        "červen - prosinec",
                                        BigDecimal.valueOf(7),
                                        BigDecimal.valueOf(4000))));

        SectionInputs otherFees =
                new OtherFeeInputs(
                        "Ostatní poplatky",
                        List.of(
                                new ServiceCost(
                                        DateRange.fromInclusive(
                                                LocalDate.of(2024, 1, 1),
                                                LocalDate.of(2024, 12, 31)),
                                        BigDecimal.valueOf(8772)),
                                new ServiceCost(
                                        DateRange.fromInclusive(
                                                LocalDate.of(2025, 1, 1),
                                                LocalDate.of(2025, 12, 31)),
                                        BigDecimal.valueOf(8000))));

        SectionInputs heatingFees =
                new HeatingFeeInputs(
                        "Vytápění",
                        List.of(
                                new ServiceCost(
                                        DateRange.fromInclusive(
                                                LocalDate.of(2024, 1, 1),
                                                LocalDate.of(2024, 12, 31)),
                                        BigDecimal.valueOf(8772)),
                                new ServiceCost(
                                        DateRange.fromInclusive(
                                                LocalDate.of(2025, 1, 1),
                                                LocalDate.of(2025, 12, 31)),
                                        BigDecimal.valueOf(8000))));

        SectionInputs coldWaterFees =
                new ColdWaterSectionInputs(
                        "Studená voda",
                        List.of(
                                new MeterReading(
                                        "Vodoměr 1",
                                        LocalDate.of(2024, 1, 1),
                                        BigDecimal.valueOf(120)),
                                new MeterReading(
                                        "Vodoměr 1",
                                        LocalDate.of(2024, 1, 31),
                                        BigDecimal.valueOf(150)),
                                new MeterReading(
                                        "Vodoměr 2",
                                        LocalDate.of(2024, 1, 1),
                                        BigDecimal.valueOf(200)),
                                new MeterReading(
                                        "Vodoměr 2",
                                        LocalDate.of(2024, 1, 31),
                                        BigDecimal.valueOf(230))),
                        List.of(
                                new WaterTariff(
                                        DateRange.fromInclusive(
                                                LocalDate.of(2024, 1, 1),
                                                LocalDate.of(2024, 12, 31)),
                                        BigDecimal.valueOf(50)),
                                new WaterTariff(
                                        DateRange.fromInclusive(
                                                LocalDate.of(2025, 1, 1),
                                                LocalDate.of(2025, 12, 31)),
                                        BigDecimal.valueOf(55))));

        SectionInputs hotWaterFees =
                new HotWaterSectionInputs(
                        "Teplá voda",
                        List.of(
                                new MeterReading(
                                        "Vodoměr T1",
                                        LocalDate.of(2024, 1, 1),
                                        BigDecimal.valueOf(300)),
                                new MeterReading(
                                        "Vodoměr T1",
                                        LocalDate.of(2024, 1, 31),
                                        BigDecimal.valueOf(330))),
                        List.of(
                                new WaterTariff(
                                        DateRange.fromInclusive(
                                                LocalDate.of(2024, 1, 1),
                                                LocalDate.of(2024, 12, 31)),
                                        BigDecimal.valueOf(50)),
                                new WaterTariff(
                                        DateRange.fromInclusive(
                                                LocalDate.of(2025, 1, 1),
                                                LocalDate.of(2025, 12, 31)),
                                        BigDecimal.valueOf(55))),
                        List.of(
                                new ServiceCost(
                                        DateRange.fromInclusive(
                                                LocalDate.of(2024, 1, 1),
                                                LocalDate.of(2024, 12, 31)),
                                        BigDecimal.valueOf(200)),
                                new ServiceCost(
                                        DateRange.fromInclusive(
                                                LocalDate.of(2025, 1, 1),
                                                LocalDate.of(2025, 12, 31)),
                                        BigDecimal.valueOf(220))),
                        List.of(
                                new WaterTariff(
                                        DateRange.fromInclusive(
                                                LocalDate.of(2024, 1, 1),
                                                LocalDate.of(2024, 12, 31)),
                                        BigDecimal.valueOf(90)),
                                new WaterTariff(
                                        DateRange.fromInclusive(
                                                LocalDate.of(2025, 1, 1),
                                                LocalDate.of(2025, 12, 31)),
                                        BigDecimal.valueOf(95))));

        DateRange dateRange =
                DateRange.fromInclusive(LocalDate.of(2024, 1, 1), LocalDate.of(2025, 12, 31));

        ReportInputs inputs =
                new ReportInputs(
                        dateRange,
                        List.of("Jan Nájemník"),
                        List.of("Karel Vlastník"),
                        "Praha",
                        LocalDate.now(),
                        List.of("Faktury, podklady měření"),
                        List.of(deposits, otherFees, heatingFees, coldWaterFees, hotWaterFees));

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
