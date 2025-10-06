package utilcalc.core.pdfGen;

import com.lowagie.text.pdf.BaseFont;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.openpdf.pdf.ITextRenderer;
import utilcalc.core.model.output.*;
import utilcalc.core.utils.ValueFormatter;

public final class PdfGenerator {

    private PdfGenerator() {}

    public static byte[] generatePdf(Report report) throws IOException {
        String htmlContent = buildHtml(report);

        ITextRenderer renderer = new ITextRenderer();
        registerFonts(renderer);
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        }
    }

    private static void registerFonts(ITextRenderer renderer) throws IOException {
        String fontResourcePath = "/liberation/LiberationSans-Regular.ttf";

        try (InputStream fontStream = PdfGenerator.class.getResourceAsStream(fontResourcePath)) {
            if (fontStream == null) {
                throw new IOException("Font " + fontResourcePath + " not found in classpath.");
            }

            File tempFontFile = File.createTempFile("liberationsans", ".ttf");
            tempFontFile.deleteOnExit();

            Files.copy(fontStream, tempFontFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            renderer.getFontResolver()
                    .addFont(
                            tempFontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        }
    }

    private static String buildHtml(Report report) {
        HtmlBuilder html = new HtmlBuilder();
        ValueFormatter formatter = html.getFormatter();

        html.h1("Vyúčtování poplatků za služby a energie")
                .p("Období: " + formatter.formatPeriod(report.dateRange()));

        for (String line : report.tenant()) {
            html.p(line);
        }

        html.h1("Celkový přehled");
        html.beginTable("Popis", "Částka");

        for (ReportSection section : report.sections()) {
            html.beginTr().tdText(section.name()).tdMoney(section.totalAmount()).endTr();
        }

        html.endTable();

        for (ReportSection section : report.sections()) {
            html.h1(section.name());

            switch (section) {
                case DepositSection depositSection -> appendDepositsTable(html, depositSection);
                case OtherFeeSection otherFeeSection -> appendOtherFeeTable(html, otherFeeSection);
                case HeatingFeeSection heatingFeeSection -> appendHeatingFeeTable(
                        html, heatingFeeSection);
                case ColdWaterSection coldWaterSection -> appendColdWaterTable(
                        html, coldWaterSection);
                case HotWaterSection hotWaterSection -> appendHotWaterTable(html, hotWaterSection);
                default -> throw new IllegalArgumentException(
                        "Unsupported section type: " + section.getClass().getSimpleName());
            }
        }

        if (!report.sources().isEmpty()) {
            html.pItalic("Zdroje:");
            for (String source : report.sources()) {
                html.pItalicIndented(source, 20);
            }
        }

        html.p(
                "Vypracováno "
                        + report.reportPlace()
                        + " dne "
                        + formatter.formatDate(report.reportDate()));

        for (String line : report.owner()) {
            html.p(line);
        }

        return html.build();
    }

    private static void waterReadings(HtmlBuilder html, List<WaterReading> readings) {
        boolean showMeterId = readings.stream().map(WaterReading::meterId).distinct().count() > 1;

        html.h2("Odečty");

        html.beginTable("Období", "Počáteční stav", "Konečný stav", "Spotřeba");

        for (WaterReading r : readings) {
            html.beginTr();

            html.beginTd().appendDateRange(r.dateRange());
            if (showMeterId) {
                html.lineBreak().append(r.meterId());
            }
            html.endTd();

            html.tdCubicMeter(r.startState());
            html.tdCubicMeter(r.endState());
            html.tdCubicMeter(r.consumption());

            html.endTr();
        }

        html.endTable();
    }

    private static void appendDepositsTable(HtmlBuilder html, DepositSection depositSection) {
        boolean unitCount =
                depositSection.deposits().stream()
                        .anyMatch(d -> d.count().compareTo(BigDecimal.ONE) != 0);

        if (unitCount) {
            html.beginTable("Popis", "Množství", "Záloha", "Částka");
        } else {
            html.beginTable("Popis", "Částka");
        }

        for (var deposit : depositSection.deposits()) {
            html.beginTr().tdText(deposit.description());

            if (unitCount) {
                html.tdMonths(deposit.count());
                html.tdCzkPerMonth(deposit.unitAmount());
            }

            html.tdMoney(deposit.amount()).endTr();
        }

        html.totalRow(unitCount ? 3 : 1, "Celkem", depositSection.totalAmount().abs());

        html.endTable();
    }

    private static void appendOtherFeeTable(HtmlBuilder html, OtherFeeSection otherFeeSection) {
        html.beginTable("Období", "Množství", "Sazba", "Cena");

        for (OtherFee fee : otherFeeSection.fees()) {
            html.beginTr()
                    .tdDateRange(fee.dateRange())
                    .tdMonths(fee.monthCount())
                    .tdCzkPerMonth(fee.monthlyCost())
                    .tdMoney(fee.feeAmount())
                    .endTr();
        }

        html.totalRow(3, "Celkem", otherFeeSection.totalAmount());

        html.endTable();
    }

    private static void appendHeatingFeeTable(
            HtmlBuilder html, HeatingFeeSection heatingFeeSection) {
        html.beginTable("Období", "Koeficient", "Sazba", "Cena");

        for (HeatingFee fee : heatingFeeSection.fees()) {
            html.beginTr()
                    .tdYearMonth(fee.yearMonth())
                    .tdNumberWithPercent(fee.monthCount(), fee.coefficient())
                    .tdCzkPerYear(fee.annualCost())
                    .tdMoney(fee.feeAmount())
                    .endTr();
        }

        html.totalRow(3, "Celkem", heatingFeeSection.totalAmount());

        html.endTable();
    }

    private static void appendColdWaterTable(HtmlBuilder html, ColdWaterSection section) {
        waterReadings(html, section.readings());

        html.h2("Náklady");
        html.beginTable("Období", "Množství", "Sazba", "Cena");

        for (WaterFee fee : section.priceList()) {
            html.beginTr()
                    .tdDateRange(fee.dateRange())
                    .tdCubicMeter(fee.quantity())
                    .tdCzkPerCubicMeter(fee.unitAmount())
                    .tdMoney(fee.periodAmount())
                    .endTr();
        }

        html.totalRow(3, "Celkem", section.totalAmount());
        html.endTable();
    }

    private static void appendHotWaterTable(HtmlBuilder html, HotWaterSection section) {
        waterReadings(html, section.readings());

        html.h2("Náklady");
        html.beginTable("Popis", "Množství", "Sazba", "Cena");

        for (WaterFee fee : section.priceList()) {
            html.beginTr()
                    .tdDateRangeWithTitle("Studená voda ", fee.dateRange())
                    .tdCubicMeter(fee.quantity())
                    .tdCzkPerCubicMeter(fee.unitAmount())
                    .tdMoney(fee.periodAmount())
                    .endTr();
        }

        for (WaterHeatingBasicPart part : section.heatingBasicParts()) {
            html.beginTr()
                    .tdDateRangeWithTitle("Ohřev základní složka ", part.dateRange())
                    .tdMonths(part.numberOfMonths())
                    .tdCzkPerMonth(part.monthlyCost())
                    .tdMoney(part.totalAmount())
                    .endTr();
        }

        for (WaterHeatingConsumablePart part : section.heatingConsumableParts()) {
            html.beginTr()
                    .tdDateRangeWithTitle("Ohřev spotřební složka ", part.dateRange())
                    .tdCubicMeter(part.unitAmount())
                    .tdCzkPerCubicMeter(part.unitCost())
                    .tdMoney(part.totalCost())
                    .endTr();
        }

        html.totalRow(3, "Celkem", section.totalAmount());

        html.endTable();
    }
}
