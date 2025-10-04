package utilcalc.cli.parser;

import static utilcalc.core.utils.FileUtil.getExtension;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import org.apache.commons.io.FilenameUtils;
import utilcalc.cli.model.AppConfiguration;
import utilcalc.core.model.enums.ExportFormat;
import utilcalc.core.model.enums.InputFormat;

@SuppressFBWarnings(value = "PATH_TRAVERSAL_IN")
public class InputArgumentParser {
    private static final ExportFormat DEFAULT_EXPORT_FORMAT = ExportFormat.PDF;
    private static final InputFormat INPUT_FORMAT = InputFormat.TOML;

    private InputArgumentParser() {}

    public static AppConfiguration parseArgument(String[] args) {
        return switch (args.length) {
            case 1 -> parseSingleArgument(args[0]);
            case 2 -> parseMultipleArgument(args);
            default -> throw new IllegalArgumentException("Too many arguments.");
        };
    }

    private static AppConfiguration parseSingleArgument(String inputFilePath) {
        String inputFileName = FilenameUtils.getName(inputFilePath);
        String outputFileName =
                inputFileName.replace(INPUT_FORMAT.getValue(), DEFAULT_EXPORT_FORMAT.getValue());
        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);

        if (!inputFile.exists() || !outputFile.exists()) return null;

        return new AppConfiguration(DEFAULT_EXPORT_FORMAT, inputFile, outputFile);
    }

    private static AppConfiguration parseMultipleArgument(String[] args) {
        ExportFormat exportFormat;
        File inputFile, outputFile;

        if (args[0].startsWith("--")) {
            exportFormat = ExportFormat.fromString(args[0].replace("--", ""));

            String inputFilePath = FilenameUtils.getName(args[1]);
            inputFile = new File(inputFilePath);
            outputFile =
                    new File(
                            inputFilePath.replace(
                                    INPUT_FORMAT.getValue(), exportFormat.getValue()));
        } else {
            exportFormat = ExportFormat.fromString(getExtension(new File(args[1])));
            inputFile = new File(FilenameUtils.getName(args[0]));
            outputFile = new File(FilenameUtils.getName(args[2]));
        }

        if (!inputFile.exists() || !outputFile.exists()) return null;

        return new AppConfiguration(exportFormat, inputFile, outputFile);
    }
}
