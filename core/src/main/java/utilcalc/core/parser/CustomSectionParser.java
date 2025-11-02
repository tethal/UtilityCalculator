package utilcalc.core.parser;

import java.math.BigDecimal;
import java.util.List;
import utilcalc.core.model.input.CustomSectionInputs;
import utilcalc.core.model.input.SectionInputs;

class CustomSectionParser {

    private CustomSectionParser() {}

    static final String SECTION_NAME = "custom";

    static SectionInputs parse(ParserUtil.GroupHeader header, List<String> lines) {

        BigDecimal totalAmount = ExprParser.parse(lines.getFirst());
        return new CustomSectionInputs(header.title(), totalAmount);
    }
}
