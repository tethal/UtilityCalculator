package utilcalc.web.report;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.output.Report;
import utilcalc.core.parser.Parser;
import utilcalc.core.pdfGen.PdfGenerator;
import utilcalc.core.reportGen.Format;
import utilcalc.core.reportGen.ReportGen;
import utilcalc.core.typstGen.TypstGenerator;
import utilcalc.web.report.request.ReportInputsRequest;

@Service
public class ReportGeneratorService {

    private final RequestToReportInputs requestToReportInputs;

    public ReportGeneratorService(RequestToReportInputs requestToReportInputs) {
        this.requestToReportInputs = requestToReportInputs;
    }

    public byte[] generate(ReportInputsRequest inputs, Format format) throws IOException {
        return generate(requestToReportInputs.convert(inputs), format);
    }

    public byte[] parseAndGenerate(String content, Format format) throws IOException {
        ReportInputs inputs = Parser.parse(content);
        return generate(inputs, format);
    }

    private byte[] generate(ReportInputs convert, Format format) throws IOException {
        Report report = ReportGen.generateReport(convert);

        return switch (format) {
            case PDF -> PdfGenerator.generatePdf(report);
            case HTML -> PdfGenerator.buildHtml(report).getBytes(StandardCharsets.UTF_8);
            case TYPEST -> TypstGenerator.generateTypst(report).getBytes(StandardCharsets.UTF_8);
        };
    }
}
