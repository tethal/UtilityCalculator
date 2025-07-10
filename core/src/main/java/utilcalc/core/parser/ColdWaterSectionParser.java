package utilcalc.core.parser;

import java.util.List;
import org.tomlj.TomlArray;
import org.tomlj.TomlTable;
import utilcalc.core.model.input.*;
import utilcalc.core.utils.Util;

class ColdWaterSectionParser {

    private ColdWaterSectionParser() {}

    static final String SECTION_NAME = "cold_water";
    static final String READING_SECTION_NAME = "reading";
    static final String TARIFF_SECTION_NAME = "tariff";
    private static final String SECTION_INPUTS_NAME = "Studená voda";

    static SectionInputs parse(Object untypedColdWaterSections) {
        TomlTable coldWaterSections = Util.castOrThrow(untypedColdWaterSections, TomlTable.class);

        TomlArray readingSections =
                ParserUtils.requireArray(coldWaterSections, READING_SECTION_NAME);
        TomlArray tariffSections = ParserUtils.requireArray(coldWaterSections, TARIFF_SECTION_NAME);

        List<MeterReading> meterReadings =
                MeterReadingParser.parse(readingSections);
        List<WaterTariff> waterTariffs =
                WaterTariffParser.parse(tariffSections);
        return new ColdWaterSectionInputs(SECTION_INPUTS_NAME, meterReadings, waterTariffs);
    }
}
