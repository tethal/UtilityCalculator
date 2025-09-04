package utilcalc.core.pdfGen;

import com.lowagie.text.pdf.BaseFont;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.openpdf.pdf.ITextRenderer;
import utilcalc.core.model.output.DepositSection;
import utilcalc.core.model.output.Report;
import utilcalc.core.model.output.ReportSection;
import utilcalc.core.utils.ReportFormatter;

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
        ReportFormatter formatter = new ReportFormatter();
        HtmlBuilder html = new HtmlBuilder();

        html.h1("Vyúčtování poplatků za služby a energie")
                .p("Období: " + formatter.formatPeriod(report.dateRange()));

        for (String line : report.tenant()) {
            html.p(line);
        }

        html.h1("Celkový přehled")
                .beginTable()
                .beginThead()
                .beginTr()
                .th("Popis")
                .th("Částka")
                .endTr()
                .endThead()
                .beginTBody();

        for (ReportSection section : report.sections()) {
            html.beginTr().td(section.name()).tdMoney(section.totalAmount()).endTr();
        }

        html.endTBody().endTable();

        for (ReportSection section : report.sections()) {
            if (section instanceof DepositSection depositSection) {
                html.h1(section.name());
                appendDepositsTable(html, depositSection);
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

    private static void appendDepositsTable(HtmlBuilder html, DepositSection depositSection) {
        boolean unitCount =
                depositSection.deposits().stream()
                        .anyMatch(d -> d.count().compareTo(BigDecimal.ONE) != 0);

        html.beginTable().beginThead().beginTr().th("Popis");
        if (unitCount) {
            html.th("Množství").th("Jednotková cena");
        }
        html.th("Částka").endTr().endThead().beginTBody();

        for (var deposit : depositSection.deposits()) {
            html.beginTr().td(deposit.description());

            if (unitCount) {
                html.td(deposit.count().toPlainString());
                html.tdMoney(deposit.unitAmount());
            }

            html.tdMoney(deposit.amount()).endTr();
        }

        html.beginTr().td("Celkem");
        if (unitCount) {
            html.td("").td("");
        }
        html.tdMoney(depositSection.totalAmount()).endTr();

        html.endTBody().endTable();
    }
}
