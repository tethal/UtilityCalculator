package utilcalc.core.parser;

import java.util.List;
import org.tomlj.TomlArray;
import utilcalc.core.model.input.HeatingFeeInputs;
import utilcalc.core.model.input.SectionInputs;
import utilcalc.core.model.input.ServiceCost;
import utilcalc.core.utils.Util;

class HeatingSectionParser {

    private HeatingSectionParser() {}

    static final String SECTION_NAME = "heating";
    private static final String SECTION_INPUTS_NAME = "Vytápění";

    static SectionInputs parse(Object untypedHeatingServiceCosts) {
        TomlArray heatingServiceCosts =
                Util.castOrThrow(untypedHeatingServiceCosts, TomlArray.class);
        List<ServiceCost> serviceCosts =
                ServiceCostsParser.parse(heatingServiceCosts, SECTION_NAME);
        return new HeatingFeeInputs(SECTION_INPUTS_NAME, serviceCosts);
    }
}
