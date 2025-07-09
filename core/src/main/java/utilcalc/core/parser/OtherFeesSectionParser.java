package utilcalc.core.parser;

import java.util.List;
import org.tomlj.TomlArray;
import utilcalc.core.model.input.OtherFeeInputs;
import utilcalc.core.model.input.SectionInputs;
import utilcalc.core.model.input.ServiceCost;
import utilcalc.core.utils.Util;

class OtherFeesSectionParser {

    private OtherFeesSectionParser() {}

    static final String SECTION_NAME = "other_fees";
    private static final String SECTION_INPUTS_NAME = "Ostatní poplatky";

    static SectionInputs parse(Object untypedOtherFeesServiceCosts) {
        TomlArray otherFeesServiceCosts =
                Util.castOrThrow(untypedOtherFeesServiceCosts, TomlArray.class);
        List<ServiceCost> serviceCosts =
                ServiceCostsParser.parse(otherFeesServiceCosts, SECTION_NAME);
        return new OtherFeeInputs(SECTION_INPUTS_NAME, serviceCosts);
    }
}
