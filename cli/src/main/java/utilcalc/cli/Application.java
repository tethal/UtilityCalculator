package utilcalc.cli;

import static utilcalc.core.parser.Parser.parse;
import static utilcalc.core.reportGen.ReportGen.generateReport;
import static utilcalc.core.utils.Util.ensureNonBlank;
import static utilcalc.core.utils.Util.ensureNonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import utilcalc.core.model.input.ReportInputs;
import utilcalc.core.model.output.Report;
import utilcalc.core.pdfGen.PdfGenerator;

public class Application {
    private static final String DEFAULT_EXPORT_FORMAT = "pdf";
    private static final String INPUT_FORMAT = "toml";

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("No parameter entered. Input file is required.");
            System.exit(1);
        }

        try {
            AppConfiguration appConfig = parseArgument(args);

            String inputFileContent = Files.readString(appConfig.inputPath());
            ReportInputs inputs = parse(inputFileContent);
            Report report = generateReport(inputs);

            byte[] exportBytes =
                    switch (appConfig.exportFormat) {
                        case "pdf" -> PdfGenerator.generatePdf(report);
                        case "html" -> PdfGenerator.buildHtml(report)
                                .getBytes(StandardCharsets.UTF_8);
                        default -> throw new IllegalArgumentException(
                                "Unexpected export format: " + appConfig.exportFormat);
                    };

            prepareOutputDirectory(appConfig.outputPath);
            Files.write(appConfig.outputPath(), exportBytes);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        System.out.println("Report completed!");
    }

    private static AppConfiguration parseArgument(String[] args) {
        return switch (args.length) {
            case 1 -> parseSingleArgument(args[0]);
            case 2 -> parseMultipleArgument(args);
            default -> throw new IllegalArgumentException("Too many arguments.");
        };
    }

    static AppConfiguration parseSingleArgument(String argument) {
        Path inputPath = Path.of(argument);
        Path outputPath = Path.of(argument.replace(getExtension(argument), DEFAULT_EXPORT_FORMAT));
        return new AppConfiguration(DEFAULT_EXPORT_FORMAT, inputPath, outputPath);
    }

    static AppConfiguration parseMultipleArgument(String[] args) {
        String exportFormat;
        Path inputPath;
        Path outputPath;

        if (args[0].startsWith("--")) {
            exportFormat = args[0].replace("--", "");
            inputPath = Path.of(args[1]);
            outputPath = Path.of(args[1].replace(INPUT_FORMAT, exportFormat));
        } else {
            exportFormat = getExtension(args[1]);
            inputPath = Path.of(args[0]);
            outputPath = Path.of(args[1]);
        }
        return new AppConfiguration(exportFormat, inputPath, outputPath);
    }

    private static String getExtension(String path) {
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < path.length() - 1) {
            return path.substring(dotIndex + 1);
        }
        throw new RuntimeException("Could not read extension file: " + path);
    }

    private static void prepareOutputDirectory(Path outputPath) throws IOException {
        Path parentDirectory = outputPath.getParent();
        if (parentDirectory != null) Files.createDirectories(parentDirectory);
    }

    record AppConfiguration(String exportFormat, Path inputPath, Path outputPath) {
        public AppConfiguration {
            ensureNonBlank(exportFormat, "Export format");
            ensureNonNull(inputPath, "Input file");
            ensureNonNull(outputPath, "Output file");
        }
    }
}
