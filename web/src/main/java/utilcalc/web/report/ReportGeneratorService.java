package utilcalc.web.report;

import java.io.IOException;
import org.springframework.stereotype.Service;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.parser.Parser;
import utilcalc.core.reportGen.Format;
import utilcalc.core.reportGen.ReportGen;

@Service
public class ReportGeneratorService {

    public byte[] parseAndGenerate(String content, Format format) throws IOException {
        ReportInputs inputs = Parser.parse(content);
        return ReportGen.generateReportInBytes(inputs, format);
    }
}
