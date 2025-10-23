package utilcalc.core.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.UnaryOperator;
import org.assertj.core.api.Assertions;

public final class TestHelpers {

    public static String getNewTestCaseContent(String name) {
        return readFile(getResourcePath("/custom/%s.uc".formatted(name)));
    }

    public static void goldenTest(String name, UnaryOperator<String> action) {
        String src = readFile(getResourcePath("/%s.uc".formatted(name)));
        Optional<String> expected =
                getResourcePathIfExists("/%s.expected".formatted(name)).map(TestHelpers::readFile);
        String actual = action.apply(src);
        if (expected.isEmpty()) {
            writeOutput(name + ".expected", actual);
            return;
        }
        if (expected.get().replaceAll("\r\n", "\n").equals(actual)) {
            deleteOutput(name + ".actual");
            return;
        }
        writeOutput(name + ".actual", actual);
        Assertions.fail("The actual output of " + name + " does not match the expected output.");
    }

    private static void writeOutput(String name, String content) {
        Path path = getPathInSrc(name);
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write actual test output: " + path, e);
        }
    }

    private static void deleteOutput(String name) {
        Path path = getPathInSrc(name);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete actual test output: " + path, e);
        }
    }

    private static Path getPathInSrc(String name) {
        return getProjectRoot()
                .resolve("core")
                .resolve("src")
                .resolve("test")
                .resolve("resources")
                .resolve(name);
    }

    private static Path getProjectRoot() {
        Path current = Path.of("").toAbsolutePath();
        while (current != null && !Files.exists(current.resolve("settings.gradle"))) {
            current = current.getParent();
        }
        if (current == null) {
            throw new RuntimeException("Could not find project root (no settings.gradle found)");
        }
        return current;
    }

    private static Path getResourcePath(String name) {
        return getResourcePathIfExists(name)
                .orElseThrow(() -> new RuntimeException("Resource not found: " + name));
    }

    private static Optional<Path> getResourcePathIfExists(String name) {
        try {
            var url = TestHelpers.class.getResource(name);
            if (url == null) {
                return Optional.empty();
            }
            return Optional.of(Path.of(url.toURI()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readFile(Path resourcePath) {
        try {
            return Files.readString(resourcePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
