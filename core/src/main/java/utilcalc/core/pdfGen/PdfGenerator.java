package utilcalc.core.pdfGen;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.openpdf.pdf.ITextRenderer;
import org.openpdf.text.pdf.BaseFont;
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

            renderer.getFontResolver().addFont(tempFontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        }
    }

    public static String buildHtml(Report report) {
        HtmlBuilder html = new HtmlBuilder();
        ValueFormatter formatter = html.getFormatter();

        html.title("Vyúčtování poplatků za služby a energie");
        html.beginCenter()
                .append("Období: ")
                .appendDateRange(report.dateRange())
                .endCenter();

        for (String line : report.tenant()) {
            html.p(line);
        }

        generateSummary(html, report);

        report.sections().forEach(s -> {
            generateSection(s, html);
        });

        html.lineBreak()
                .append(report.reportPlace() + ", " + formatter.formatDate(report.reportDate()))
                .lineBreak();

        report.owner().forEach(html::p);

        return html.build();
    }

    private static void generateSummary(HtmlBuilder html, Report report) {
        html.beginSection("Celkový přehled");
        html.beginTable("Popis", "Částka");
        report.sections()
                .forEach(s ->
                        html.beginTr().tdText(s.name()).tdMoney(s.totalAmount()).endTr());
        html.totalRow(
                1,
                report.total().signum() == -1 ? "Přeplatek" : "Nedoplatek",
                report.total().abs());
        html.endTable();
        html.endSection();

        if (!report.sources().isEmpty()) {
            html.pItalic("Zdroje:");
            report.sources().forEach(s -> html.pItalicIndented(s, 20));
            html.lineBreak();
        }
    }

    private static void generateSection(ReportSection section, HtmlBuilder html) {
        switch (section) {
            case ColdWaterSection s -> appendColdWaterTable(html, s);
            case HotWaterSection s -> appendHotWaterTable(html, s);
            case HeatingFeeSection s -> appendHeatingFeeTable(html, s);
            case OtherFeeSection s -> appendOtherFeeTable(html, s);
            case DepositSection s -> appendDepositsTable(html, s);
            case CustomSection ignore -> {}
            default -> throw new IllegalStateException("Unexpected section: " + section);
        }
    }

    private static void waterReadings(HtmlBuilder html, List<WaterReading> readings) {
        boolean showMeterId =
                readings.stream().map(WaterReading::meterId).distinct().count() > 1;

        html.h3("Odečty");

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
                depositSection.deposits().stream().anyMatch(d -> d.count().compareTo(BigDecimal.ONE) != 0);

        html.beginSection(depositSection.name());

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
        html.endSection();
    }

    private static void appendOtherFeeTable(HtmlBuilder html, OtherFeeSection otherFeeSection) {
        html.beginSection(otherFeeSection.name());

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
        html.endSection();
    }

    private static void appendHeatingFeeTable(HtmlBuilder html, HeatingFeeSection heatingFeeSection) {
        html.beginSection(heatingFeeSection.name());

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
        html.endSection();
    }

    private static void appendColdWaterTable(HtmlBuilder html, ColdWaterSection section) {
        html.beginSection(section.name());
        waterReadings(html, section.readings());
        html.endSection();

        html.beginSection("Náklady", 3);
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
        html.endSection();
    }

    private static void appendHotWaterTable(HtmlBuilder html, HotWaterSection section) {
        html.beginSection(section.name());
        waterReadings(html, section.readings());
        html.endSection();

        html.beginSection("Náklady", 3);
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
        html.endSection();
    }
}
