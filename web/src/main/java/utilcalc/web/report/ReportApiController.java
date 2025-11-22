package utilcalc.web.report;

import static utilcalc.web.WebUtil.extensionFor;
import static utilcalc.web.WebUtil.sanitizeFilename;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utilcalc.core.reportGen.Format;
import utilcalc.web.WebUtil;
import utilcalc.web.report.request.ReportInputsRequest;

@RequestMapping("/api")
@RestController
public class ReportApiController {

    private final ReportGeneratorService reportGeneratorService;

    public ReportApiController(ReportGeneratorService reportGeneratorService) {
        this.reportGeneratorService = reportGeneratorService;
    }

    @PostMapping(value = "/generate-report", consumes = "application/json")
    public ResponseEntity<byte[]> generateReportFromJson(
            @RequestParam(value = "format", defaultValue = "pdf") Format format,
            @RequestParam(value = "filename", defaultValue = "report") String filename,
            @RequestBody @Valid ReportInputsRequest inputs)
            throws IOException {
        byte[] generatedReport = reportGeneratorService.generate(inputs, format);

        HttpHeaders headers = constructHeadersForResponse(format, filename);

        return ResponseEntity.ok().headers(headers).body(generatedReport);
    }

    @PostMapping(value = "/generate-report", consumes = "application/uc")
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
        headers.setContentType(WebUtil.mediaTypeFor(format));
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(constructFullFilename(format, filename))
                .build());
        return headers;
    }

    private static String constructFullFilename(Format format, String filename) {
        String safeName = sanitizeFilename(filename);
        String ext = extensionFor(format);
        return safeName + "." + ext;
    }
}
