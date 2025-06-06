package utilcalc.core.parser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

final class ParserTestHelper {

    private static final String BASE_PATH = "/toml/";
    private static final String FILE_EXTENSION = "toml";

    static String getTestCaseContent(String name) {
        String resourcePath = BASE_PATH + name + "." + FILE_EXTENSION;
        try {
            var url = ParserTestHelper.class.getResource(resourcePath);
            if (url == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            Path path = Path.of(url.toURI());
            return Files.readString(path);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to read test case file: " + resourcePath, e);
        }
    }
}
