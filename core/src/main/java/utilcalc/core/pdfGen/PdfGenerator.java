package utilcalc.core.pdfGen;

import java.io.*;
import org.xhtmlrenderer.pdf.ITextRenderer;
import utilcalc.core.model.output.Report;
import utilcalc.core.utils.ReportFormatter;
import com.lowagie.text.pdf.BaseFont;

public final class PdfGenerator {

    private PdfGenerator() {}

    public static byte[] generatePdf(Report report) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            String htmlContent = buildHtml(report);

            ITextRenderer renderer = new ITextRenderer();
            registerFonts(renderer);

            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IOException("Failed to generate PDF", e);
        }
    }

    private static void registerFonts(ITextRenderer renderer) throws IOException {
        try (InputStream fontStream = PdfGenerator.class.getResourceAsStream("/fonts/DejaVuSans.ttf")) {
            if (fontStream == null) {
                throw new IOException("Font DejaVuSans.ttf nebyl nalezen v resources.");
            }

            File tempFontFile = File.createTempFile("dejavusans", ".ttf");
            tempFontFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFontFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fontStream.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                }
            }

            renderer.getFontResolver().addFont(
                    tempFontFile.getAbsolutePath(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );
        }
    }

    private static String buildHtml(Report report) {
        ReportFormatter formatter = new ReportFormatter();
        HtmlBuilder html = new HtmlBuilder();

        html.h1("Vyúčtování poplatků za služby a energie")
                .p("Období: " + formatter.formatPeriod(report.startDate(), report.endDate()));

        for (String line : report.tenant()) {
            html.p(line);
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
}
