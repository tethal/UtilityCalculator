package utilcalc.core.parser;

import java.math.BigDecimal;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.WaterTariff;

class WaterTariffParser {

	private WaterTariffParser() {
	}

	static WaterTariff parseLine(String line) {
		String[] parts = line.split(":", 2);
		if (parts.length != 2) {
			throw new ParsingException("Invalid water tariff line (missing ':'): " + line);
		}

		DateRange range = ParserUtil.parseRange(parts[0].strip());
		BigDecimal unitAmount = ExprParser.parse(parts[1].strip());

		return new WaterTariff(range, unitAmount);
	}
}
