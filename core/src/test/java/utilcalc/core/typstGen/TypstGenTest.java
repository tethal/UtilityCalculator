package utilcalc.core.typstGen;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utilcalc.core.parser.Parser;
import utilcalc.core.reportGen.ReportGen;
import utilcalc.core.util.TestHelpers;

class TypstGenTest {

    @ValueSource(strings = {"2017", "2023"})
    @ParameterizedTest
    void testGoldenFiles(String name) {
        TestHelpers.goldenTest(
                "typst/" + name, src -> TypstGenerator.generateTypst(ReportGen.generateReport(Parser.parse(src))));
    }
}
