package utilcalc.core.parser;

import static utilcalc.core.parser.ParserUtils.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import org.tomlj.TomlArray;
import org.tomlj.TomlTable;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.ServiceCost;

class ServiceCostsParser {

    private ServiceCostsParser() {}

    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String ANNUAL_COST = "annual_cost";

    private static final Set<String> SERVICE_COST_KNOWN_FIELDS =
            Set.of(START_DATE, END_DATE, ANNUAL_COST);

    static List<ServiceCost> parse(TomlArray serviceCosts, String sectionName) {
        return IntStream.range(0, serviceCosts.size())
                .mapToObj(serviceCosts::getTable)
                .map(serviceCost -> parseServiceCost(serviceCost, sectionName))
                .toList();
    }

    private static ServiceCost parseServiceCost(TomlTable serviceCostTable, String sectionName) {
        checkThatSectionContainsOnlyKnownFields(
                serviceCostTable, SERVICE_COST_KNOWN_FIELDS, sectionName);
        DateRange dateRange = requireDateRange(serviceCostTable, START_DATE, END_DATE);
        BigDecimal annualCost = requireBigDecimal(serviceCostTable, ANNUAL_COST);
        return new ServiceCost(dateRange, annualCost);
    }
}
