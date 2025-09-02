package utilcalc.core.parser;

import java.util.Comparator;
import java.util.List;
import org.tomlj.TomlTable;
import utilcalc.core.model.input.*;

class SectionInputsParser {

    private static final List<Class<? extends SectionInputs>> SECTION_ORDER =
            List.of(
                    ColdWaterSectionInputs.class,
                    HotWaterSectionInputs.class,
                    HeatingFeeInputs.class,
                    OtherFeeInputs.class,
                    DepositsSectionInputs.class);

    private SectionInputsParser() {}

    static List<SectionInputs> parse(TomlTable parseResult) {
        return parseResult.entrySet().stream()
                .filter(entry -> !Parser.GENERAL_SECTION_NAME.equals(entry.getKey()))
                .map(entry -> parserSectionInputs(entry.getValue(), entry.getKey()))
                .sorted(Comparator.comparing(section -> SECTION_ORDER.indexOf(section.getClass())))
                .toList();
    }

    private static SectionInputs parserSectionInputs(Object section, String sectionName) {
        return switch (sectionName) {
            case DepositsSectionParser.SECTION_NAME -> DepositsSectionParser.parse(section);
            case HeatingSectionParser.SECTION_NAME -> HeatingSectionParser.parse(section);
            case OtherFeesSectionParser.SECTION_NAME -> OtherFeesSectionParser.parse(section);
            case ColdWaterSectionParser.SECTION_NAME -> ColdWaterSectionParser.parse(section);
            case WaterHeatingSectionParser.SECTION_NAME -> WaterHeatingSectionParser.parse(section);
            default -> throw new ParsingException("Unknown section " + sectionName);
        };
    }
}
