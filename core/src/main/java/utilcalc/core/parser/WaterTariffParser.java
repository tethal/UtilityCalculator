package utilcalc.core.parser;

import static utilcalc.core.parser.ParserUtils.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import org.tomlj.TomlArray;
import org.tomlj.TomlTable;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.WaterTariff;

class WaterTariffParser {

    private WaterTariffParser() {}

    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String UNIT_AMOUNT = "unit_amount";

    private static final Set<String> WATER_TARIFF_KNOWN_FIELDS =
            Set.of(START_DATE, END_DATE, UNIT_AMOUNT);

    static List<WaterTariff> parse(TomlArray waterTariffs) {
        return IntStream.range(0, waterTariffs.size())
                .mapToObj(waterTariffs::getTable)
                .map(WaterTariffParser::parseWaterTariff)
                .toList();
    }

    private static WaterTariff parseWaterTariff(TomlTable waterTariffTable) {
        checkThatSectionContainsOnlyKnownFields(
                waterTariffTable,
                WATER_TARIFF_KNOWN_FIELDS,
                ColdWaterSectionParser.TARIFF_SECTION_NAME);
        DateRange dateRange = requireDateRange(waterTariffTable, START_DATE, END_DATE);
        BigDecimal pricePerCubicMeter = requireBigDecimal(waterTariffTable, UNIT_AMOUNT);
        return new WaterTariff(dateRange, pricePerCubicMeter);
    }
}
