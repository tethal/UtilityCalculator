package utilcalc.web.report;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utilcalc.core.reportGen.Format;

@RequestMapping("/api")
@RestController
public class ReportApiController {

    private final ReportGeneratorService reportGeneratorService;

    public ReportApiController(ReportGeneratorService reportGeneratorService) {
        this.reportGeneratorService = reportGeneratorService;
    }

    @PostMapping(value = "/generate-report", consumes = "x-application/uc")
    public ResponseEntity<byte[]> generateReportFromUc(
            @RequestParam(value = "format", defaultValue = "pdf") Format format,
            @RequestParam(value = "filename", defaultValue = "report") String filename,
            @NotNull @Valid @RequestBody String content)
            throws IOException {
        byte[] generatedReport = reportGeneratorService.parseAndGenerate(content, format);

        HttpHeaders headers = constructHeadersForResponse(format, filename);

        return ResponseEntity.ok().headers(headers).body(generatedReport);
    }

    private static HttpHeaders constructHeadersForResponse(Format format, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaTypeFor(format));
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(constructFullFilename(format, filename))
                .build());
        return headers;
    }

    private static String constructFullFilename(Format format, String filename) {
        String safeName = sanitizeFilename(filename);
        String ext = format.getExtension();
        return safeName + "." + ext;
    }

    private static MediaType mediaTypeFor(Format format) {
        return switch (format) {
            case PDF -> MediaType.APPLICATION_PDF;
            case HTML -> MediaType.TEXT_HTML;
            case TYPST -> MediaType.valueOf("text/vnd.typst");
        };
    }

    private static String sanitizeFilename(String input) {
        return input.replaceAll("[\\\\/\\r\\n\\t]", "") // odstraní slashes a control char
                .replaceAll("^\\.+", "") // žádné tečky na začátku
                .trim();
    }
}
