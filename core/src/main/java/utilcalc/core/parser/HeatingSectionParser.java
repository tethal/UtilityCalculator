package utilcalc.core.parser;

import static utilcalc.core.parser.ParserUtil.titleOrDefault;

import java.util.List;
import utilcalc.core.model.input.HeatingFeeInputs;
import utilcalc.core.model.input.SectionInputs;
import utilcalc.core.model.input.ServiceCost;

class HeatingSectionParser {

    private HeatingSectionParser() {}

    static final String SECTION_NAME = "vytapeni";
    private static final String SECTION_INPUTS_TITLE = "Vytápění";

    static SectionInputs parse(ParserUtil.GroupHeader header, List<String> lines) {
        List<ServiceCost> serviceCosts = ServiceCostsParser.parse(lines);
        return new HeatingFeeInputs(titleOrDefault(header, SECTION_INPUTS_TITLE), serviceCosts);
    }
}
