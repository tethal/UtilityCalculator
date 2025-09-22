package utilcalc.cli.model;

import static utilcalc.core.utils.Util.ensureNonNull;

import java.io.File;
import utilcalc.core.model.enums.ExportFormat;

public record AppConfiguration(ExportFormat exportFormat, File inputFile, File outputFile) {
    public AppConfiguration {
        ensureNonNull(exportFormat, "Export format");
        ensureNonNull(inputFile, "Input file");
        ensureNonNull(outputFile, "Output file");
    }
}
