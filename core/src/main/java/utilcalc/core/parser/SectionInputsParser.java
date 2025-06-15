package utilcalc.core.parser;

import java.util.List;
import java.util.Map;
import org.tomlj.TomlArray;
import org.tomlj.TomlTable;
import utilcalc.core.model.input.SectionInputs;

class SectionInputsParser {

    private SectionInputsParser() {}

    static List<SectionInputs> parse(TomlTable parseResult) {
        return parseResult.entrySet().stream()
                .filter(entry -> !Parser.GENERAL_SECTION_NAME.equals(entry.getKey()))
                .map(entry -> parserSectionInputs(getTomlArray(entry), entry.getKey()))
                .toList();
    }

    private static SectionInputs parserSectionInputs(TomlArray section, String sectionName) {
        return switch (sectionName) {
            case DepositsSectionParser.SECTION_NAME -> DepositsSectionParser.parse(section);
            case HeatingSectionParser.SECTION_NAME -> HeatingSectionParser.parse(section);
            case OtherFeesSectionParser.SECTION_NAME -> OtherFeesSectionParser.parse(section);
            default -> throw new ParsingException("Unknown section " + sectionName);
        };
    }

    private static TomlArray getTomlArray(Map.Entry<String, Object> entry) {
        if (entry.getValue() instanceof TomlArray array) {
            return array;
        }
        throw new IllegalArgumentException("Expected TomlArray for section " + entry.getKey());
    }
}
