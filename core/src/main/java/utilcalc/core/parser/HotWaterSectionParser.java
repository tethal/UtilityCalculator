package utilcalc.core.parser;

import java.util.ArrayList;
import java.util.List;
import utilcalc.core.model.input.*;

class HotWaterSectionParser {

	private HotWaterSectionParser() {
	}

	static final String SECTION_NAME = "tepla voda";
	private static final String SECTION_INPUTS_TITLE = "Teplá voda";

	static SectionInputs parse(ParserUtil.GroupHeader header, List<String> lines) {
		List<MeterReading> meterReadings = new ArrayList<>();
		List<WaterTariff> waterTariffs = new ArrayList<>();
		List<ServiceCost> heatingBasicCosts = new ArrayList<>();
		List<WaterTariff> heatingConsumableTariffs = new ArrayList<>();

		for (String line : lines) {
			if (line.startsWith("O")) {
				// O 2024-01-01: TV1 @ 14.5, TV2 @ 55.6
				String withoutPrefix = line.substring(1).strip();
				meterReadings.addAll(MeterReadingParser.parseLine(withoutPrefix, "TV"));
			} else if (line.startsWith("SV")) {
				// SV 2024: 5920.16 / 42.2
				String withoutPrefix = line.substring(2).strip();
				waterTariffs.add(WaterTariffParser.parseLine(withoutPrefix));
			} else if (line.startsWith("ZS")) {
				// ZS 2024: 2725.92
				String withoutPrefix = line.substring(2).strip();
				heatingBasicCosts.add(ServiceCostsParser.parseSingleLine(withoutPrefix));
			} else if (line.startsWith("SS")) {
				// SS 2024: 257.988
				String withoutPrefix = line.substring(2).strip();
				heatingConsumableTariffs.add(WaterTariffParser.parseLine(withoutPrefix));
			} else {
				throw new ParsingException("Unknown line type in [tepla voda]: " + line);
			}
		}

		return new HotWaterSectionInputs(ParserUtil.titleOrDefault(header, SECTION_INPUTS_TITLE), meterReadings,
				waterTariffs, heatingBasicCosts, heatingConsumableTariffs);
	}
}
