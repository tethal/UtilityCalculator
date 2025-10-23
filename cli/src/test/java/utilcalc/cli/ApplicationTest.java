package utilcalc.cli;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class ApplicationTest {

    @Test
    void parseSingleArgument_withInputFileName_should_haveCorrectAppConfiguration() {
        String argument = "inputFile.uc";

        Application.AppConfiguration appConfiguration = Application.parseSingleArgument(argument);

        assertThat(appConfiguration.exportFormat()).isEqualTo("pdf");
        assertThat(appConfiguration.inputPath()).isEqualTo(Path.of("inputFile.uc"));
        assertThat(appConfiguration.outputPath()).isEqualTo(Path.of("inputFile.pdf"));
    }

    @Test
    void parseSingleArgument_withInputPath_should_haveCorrectAppConfiguration() {
        String argument = "user/documents/inputFile.uc";

        Application.AppConfiguration appConfiguration = Application.parseSingleArgument(argument);

        assertThat(appConfiguration.exportFormat()).isEqualTo("pdf");
        assertThat(appConfiguration.inputPath()).isEqualTo(Path.of("user/documents/inputFile.uc"));
        assertThat(appConfiguration.outputPath())
                .isEqualTo(Path.of("user/documents/inputFile.pdf"));
    }

    @Test
    void parseMultipleArgument_withExtensionAndInputPath_should_haveCorrectAppConfiguration() {
        String[] args = {"--pdf", "inputFile.uc"};

        Application.AppConfiguration appConfiguration = Application.parseMultipleArgument(args);

        assertThat(appConfiguration.exportFormat()).isEqualTo("pdf");
        assertThat(appConfiguration.inputPath()).isEqualTo(Path.of("inputFile.uc"));
        assertThat(appConfiguration.outputPath()).isEqualTo(Path.of("inputFile.pdf"));
    }

    @Test
    void parseMultipleArgument_withInputPathAndOutputPath_should_haveCorrectAppConfiguration() {
        String[] args = {"inputFile.uc", "outputFile.pdf"};

        Application.AppConfiguration appConfiguration = Application.parseMultipleArgument(args);

        assertThat(appConfiguration.exportFormat()).isEqualTo("pdf");
        assertThat(appConfiguration.inputPath()).isEqualTo(Path.of("inputFile.uc"));
        assertThat(appConfiguration.outputPath()).isEqualTo(Path.of("outputFile.pdf"));
    }
}
