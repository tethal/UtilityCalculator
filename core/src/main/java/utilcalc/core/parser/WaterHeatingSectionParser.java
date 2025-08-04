package utilcalc.core.parser;

import java.util.List;
import org.tomlj.TomlArray;
import org.tomlj.TomlTable;
import utilcalc.core.model.input.*;
import utilcalc.core.utils.Util;

class WaterHeatingSectionParser {

    private WaterHeatingSectionParser() {}

    static final String SECTION_NAME = "hot_water";
    static final String READING_SECTION_NAME = "reading";
    static final String TARIFF_SECTION_NAME = "tariff";
    static final String HEATING_BASIC_COST_SECTION_NAME = "heating_basic_cost";
    static final String HEATING_CONSUMABLE_TARIFF_SECTION_NAME = "heating_consumable_tariff";
    private static final String SECTION_INPUTS_NAME = "Teplá voda";

    static SectionInputs parse(Object untypedWaterHeatingSections) {
        TomlTable waterHeatingSections =
                Util.castOrThrow(untypedWaterHeatingSections, TomlTable.class);

        TomlArray readingSections =
                ParserUtils.requireArray(waterHeatingSections, READING_SECTION_NAME);
        TomlArray tariffSections =
                ParserUtils.requireArray(waterHeatingSections, TARIFF_SECTION_NAME);
        TomlArray heatingBasicCostSections =
                ParserUtils.requireArray(waterHeatingSections, HEATING_BASIC_COST_SECTION_NAME);
        TomlArray heatingConsumableTariffSections =
                ParserUtils.requireArray(
                        waterHeatingSections, HEATING_CONSUMABLE_TARIFF_SECTION_NAME);

        List<MeterReading> meterReadings = MeterReadingParser.parse(readingSections);
        List<WaterTariff> waterTariffs =
                WaterTariffParser.parse(tariffSections, TARIFF_SECTION_NAME);
        List<ServiceCost> heatingBasicCosts =
                ServiceCostsParser.parse(heatingBasicCostSections, HEATING_BASIC_COST_SECTION_NAME);
        List<WaterTariff> heatingConsumableTariffs =
                WaterTariffParser.parse(
                        heatingConsumableTariffSections, HEATING_CONSUMABLE_TARIFF_SECTION_NAME);
        return new HotWaterSectionInputs(
                SECTION_INPUTS_NAME,
                meterReadings,
                waterTariffs,
                heatingBasicCosts,
                heatingConsumableTariffs);
    }
}
