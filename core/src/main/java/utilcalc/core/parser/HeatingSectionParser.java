package utilcalc.core.parser;

import java.util.List;
import org.tomlj.TomlArray;
import utilcalc.core.model.input.HeatingFeeInputs;
import utilcalc.core.model.input.SectionInputs;
import utilcalc.core.model.input.ServiceCost;

class HeatingSectionParser {

    private HeatingSectionParser() {}

    static final String SECTION_NAME = "heating";
    private static final String SECTION_INPUTS_NAME = "Vytápění";

    static SectionInputs parse(TomlArray heatingServiceCosts) {
        List<ServiceCost> serviceCosts =
                ServiceCostsParser.parse(heatingServiceCosts, SECTION_NAME);
        return new HeatingFeeInputs(SECTION_INPUTS_NAME, serviceCosts);
    }
}
