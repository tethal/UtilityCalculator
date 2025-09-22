package utilcalc.cli.application;

import static utilcalc.core.utils.FileUtil.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import utilcalc.cli.model.AppConfiguration;
import utilcalc.core.model.enums.ExportFormat;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.output.Report;
import utilcalc.core.parser.Parser;
import utilcalc.core.pdfGen.PdfGenerator;
import utilcalc.core.reportGen.ReportGen;

public class ApplicationRunner {

    ApplicationRunner() {}

    public static void run(AppConfiguration appConfig) {
        ReportInputs inputs = parseInput(appConfig.inputFile());
        Report report = generateReport(inputs);
        byte[] exportBytes = exportReport(report, appConfig.exportFormat());
        prepareOutputDirectory(appConfig.outputFile());
        writeOutput(appConfig.outputFile(), exportBytes);
    }

    private static ReportInputs parseInput(File inputFile) {
        try {
            String content = Files.readString(Path.of(inputFile.getAbsolutePath()));
            return Parser.parse(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input file: " + inputFile, e);
        }
    }

    private static Report generateReport(ReportInputs inputs) {
        try {
            return ReportGen.generateReport(inputs);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate report", e);
        }
    }

    private static byte[] exportReport(Report report, ExportFormat format) {
        try {
            return switch (format) {
                case PDF -> PdfGenerator.generatePdf(report);
                case HTML -> PdfGenerator.buildHtml(report).getBytes();
            };
        } catch (Exception e) {
            throw new RuntimeException("Failed to export report as " + format.getValue(), e);
        }
    }
}
