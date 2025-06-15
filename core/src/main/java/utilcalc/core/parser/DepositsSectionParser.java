package utilcalc.core.parser;

import static utilcalc.core.parser.ParserUtils.*;
import static utilcalc.core.parser.ParserUtils.requireBigDecimal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import org.tomlj.TomlArray;
import org.tomlj.TomlTable;
import utilcalc.core.model.input.DepositsSectionInputs;
import utilcalc.core.model.input.Payment;
import utilcalc.core.model.input.SectionInputs;

class DepositsSectionParser {

    private DepositsSectionParser() {}

    static final String SECTION_NAME = "deposits";
    private static final String SECTION_INPUTS_NAME = "Přijaté zálohy";

    private static final String DESCRIPTION = "description";
    private static final String COUNT = "count";
    private static final String AMOUNT = "amount";

    private static final Set<String> SECTION_KNOWN_FIELDS = Set.of(DESCRIPTION, COUNT, AMOUNT);

    static SectionInputs parse(TomlArray depositPayments) {
        List<Payment> payments =
                IntStream.range(0, depositPayments.size())
                        .mapToObj(depositPayments::getTable)
                        .map(DepositsSectionParser::parsePayment)
                        .toList();

        return new DepositsSectionInputs(SECTION_INPUTS_NAME, payments);
    }

    private static Payment parsePayment(TomlTable payment) {
        checkThatSectionContainsOnlyKnownFields(payment, SECTION_KNOWN_FIELDS, SECTION_NAME);
        String description = requireString(payment, DESCRIPTION);
        BigDecimal count = requireBigDecimal(payment, COUNT, () -> BigDecimal.ONE);
        BigDecimal amount = requireBigDecimal(payment, AMOUNT);
        return new Payment(description, count, amount);
    }
}
