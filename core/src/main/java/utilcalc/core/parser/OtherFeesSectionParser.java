package utilcalc.core.parser;

import static utilcalc.core.parser.ParserUtil.titleOrDefault;

import java.util.List;
import utilcalc.core.model.input.OtherFeeInputs;
import utilcalc.core.model.input.SectionInputs;
import utilcalc.core.model.input.ServiceCost;

class OtherFeesSectionParser {

    private OtherFeesSectionParser() {}

    static final String SECTION_NAME = "ostatni poplatky";
    private static final String SECTION_INPUTS_TITLE = "Ostatní poplatky";

    static SectionInputs parse(ParserUtil.GroupHeader header, List<String> lines) {
        List<ServiceCost> serviceCosts = ServiceCostsParser.parse(lines);
        return new OtherFeeInputs(titleOrDefault(header, SECTION_INPUTS_TITLE), serviceCosts);
    }
}
