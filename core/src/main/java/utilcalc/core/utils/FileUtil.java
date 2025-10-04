package utilcalc.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class FileUtil {

    private FileUtil() {}

    public static String getExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < name.length() - 1) {
            return name.substring(dotIndex + 1);
        }
        throw new RuntimeException("Could not read extension file: " + file);
    }

    public static void prepareOutputDirectory(File outputFile) {
        File parentDir = outputFile.getAbsoluteFile().getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new RuntimeException(
                        "Could not create directory: " + parentDir.getAbsolutePath());
            }
        }
    }

    public static void writeOutput(File outputFile, byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Exported bytes must not be null");
        }
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            out.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write output file: " + outputFile, e);
        }
    }
}
