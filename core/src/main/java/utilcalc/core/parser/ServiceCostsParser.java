package utilcalc.core.parser;

import java.math.BigDecimal;
import java.util.List;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.ServiceCost;

class ServiceCostsParser {

    static List<ServiceCost> parse(List<String> lines) {
        return lines.stream().map(ServiceCostsParser::parseServiceCostLine).toList();
    }

    static ServiceCost parseSingleLine(String line) {
        return parseServiceCostLine(line);
    }

    private static ServiceCost parseServiceCostLine(String line) {
        String[] parts = line.split(":", 2);
        if (parts.length != 2) {
            throw new ParsingException("Invalid service cost line (missing ':'): " + line);
        }

        DateRange range = ParserUtil.parseRange(parts[0].strip());
        BigDecimal annualCost = ExprParser.parse(parts[1].strip());

        return new ServiceCost(range, annualCost);
    }
}
