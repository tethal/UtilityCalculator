package utilcalc.core.parser;

import java.util.List;
import org.tomlj.TomlTable;
import utilcalc.core.model.input.SectionInputs;

class SectionInputsParser {

    private SectionInputsParser() {}

    static List<SectionInputs> parse(TomlTable parseResult) {
        return parseResult.entrySet().stream()
                .filter(entry -> !Parser.GENERAL_SECTION_NAME.equals(entry.getKey()))
                .map(entry -> parserSectionInputs(entry.getValue(), entry.getKey()))
                .toList();
    }

    private static SectionInputs parserSectionInputs(Object section, String sectionName) {
        return switch (sectionName) {
            case DepositsSectionParser.SECTION_NAME -> DepositsSectionParser.parse(section);
            case HeatingSectionParser.SECTION_NAME -> HeatingSectionParser.parse(section);
            case OtherFeesSectionParser.SECTION_NAME -> OtherFeesSectionParser.parse(section);
            case ColdWaterSectionParser.SECTION_NAME -> ColdWaterSectionParser.parse(section);
            default -> throw new ParsingException("Unknown section " + sectionName);
        };
    }
}
