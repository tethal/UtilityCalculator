package utilcalc.core.parser.newparser;

import static utilcalc.core.parser.newparser.ParserUtil.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import utilcalc.core.model.input.DepositsSectionInputs;
import utilcalc.core.model.input.Payment;
import utilcalc.core.model.input.SectionInputs;
import utilcalc.core.parser.ParsingException;

class DepositsSectionParser {

    private DepositsSectionParser() {}

    static final String SECTION_NAME = "zalohy";
    private static final String SECTION_INPUTS_TITLE = "Přijaté zálohy";

    static SectionInputs parse(GroupHeader header, List<String> lines) {
        List<Payment> payments = new ArrayList<>();

        for (String line : lines) {
            PaymentLine paymentLine = parsePaymentLine(line);

            BigDecimal count;
            BigDecimal unitAmount;

            if (paymentLine.amountPart().contains("x")) {
                String[] countAndAmount = paymentLine.amountPart().split("x", 2);
                if (countAndAmount.length != 2) {
                    throw new ParsingException(
                            "Invalid payment format (missing RHS after x): " + line);
                }
                count = ExprParser.parse(countAndAmount[0].trim());
                unitAmount = ExprParser.parse(countAndAmount[1].trim());
            } else {
                count = BigDecimal.ONE;
                unitAmount = ExprParser.parse(paymentLine.amountPart());
            }

            payments.add(new Payment(paymentLine.description(), count, unitAmount));
        }

        return new DepositsSectionInputs(titleOrDefault(header, SECTION_INPUTS_TITLE), payments);
    }

    private static PaymentLine parsePaymentLine(String line) {
        String[] parts = line.split(":", 2);
        if (parts.length != 2) {
            throw new ParsingException("Invalid payment line: " + line);
        }

        return new PaymentLine(parts[0].strip(), parts[1].strip());
    }

    record PaymentLine(String description, String amountPart) {}
}
