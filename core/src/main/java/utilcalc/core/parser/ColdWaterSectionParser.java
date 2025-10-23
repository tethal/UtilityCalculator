package utilcalc.core.parser;

import java.util.ArrayList;
import java.util.List;
import utilcalc.core.model.input.*;

class ColdWaterSectionParser {

    private ColdWaterSectionParser() {}

    static final String SECTION_NAME = "studena voda";
    private static final String SECTION_INPUTS_TITLE = "Studená voda";

    static SectionInputs parse(ParserUtil.GroupHeader header, List<String> lines) {
        List<MeterReading> meterReadings = new ArrayList<>();
        List<WaterTariff> waterTariffs = new ArrayList<>();

        for (String line : lines) {
            if (line.startsWith("O")) {
                // O 2024-01-01: TV1 @ 14.5, TV2 @ 55.6
                String withoutPrefix = line.substring(1).strip();
                meterReadings.addAll(MeterReadingParser.parseLine(withoutPrefix, "SV"));
            } else if (line.startsWith("SV")) {
                // SV 2024: 5920.16 / 42.2
                String withoutPrefix = line.substring(2).strip();
                waterTariffs.add(WaterTariffParser.parseLine(withoutPrefix));
            } else {
                throw new ParsingException("Unknown line type in [studena voda]: " + line);
            }
        }

        return new ColdWaterSectionInputs(
                ParserUtil.titleOrDefault(header, SECTION_INPUTS_TITLE),
                meterReadings,
                waterTariffs);
    }
}
