package utilcalc.core.pdfGen;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import org.junit.jupiter.api.Test;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.lowagie.text.pdf.BaseFont;

public class MinimalPdfDiacriticsTest {

    @Test
    void generatePdf_withCzechDiacritics_shouldCreateNonEmptyPdf() throws Exception {
        String html = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8" />
            <style>
                body {
                    font-family: 'DejaVu Sans', sans-serif;
                    font-size: 14pt;
                }
            </style>
        </head>
        <body>
            <p>Česká diakritika: ěščřžýáíéúů</p>
        </body>
        </html>
        """;

        ITextRenderer renderer = new ITextRenderer();

        try (InputStream fontStream = getClass().getResourceAsStream("/fonts/DejaVuSans.ttf")) {
            assertNotNull(fontStream, "Font DejaVuSans.ttf nebyl nalezen v resources.");

            File tempFontFile = File.createTempFile("dejavusans", ".ttf");
            tempFontFile.deleteOnExit();

            try (OutputStream out = new FileOutputStream(tempFontFile)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fontStream.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            }

            renderer.getFontResolver().addFont(
                    tempFontFile.getAbsolutePath(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );
        }

        renderer.setDocumentFromString(html, null);
        renderer.layout();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        renderer.createPDF(baos);

        byte[] pdfBytes = baos.toByteArray();

        assertNotNull(pdfBytes, "PDF bytes nesmí být null.");
        assertTrue(pdfBytes.length > 100, "Vygenerované PDF je příliš malé.");

        File outputFile = new File("build/test-output/test-diacritics.pdf");
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                throw new IOException("Nepodařilo se vytvořit adresář: " + parentDir.getAbsolutePath());
            }
        }
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(pdfBytes);
        }
    }
}
