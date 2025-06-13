package utilcalc.core.reportGen;

import java.math.BigDecimal;
import utilcalc.core.model.input.Payment;

final class PaymentFactory {

    private PaymentFactory() {}

    public static Payment validPayment1() {
        return new Payment("Leden - Červen", new BigDecimal("6"), new BigDecimal("500"));
    }

    public static Payment validPayment2() {
        return new Payment("Červen - Září", new BigDecimal("4"), new BigDecimal("600"));
    }

    public static Payment invalidPaymentWithNegativeCount() {
        return new Payment("Leden - Červen", new BigDecimal("-6"), new BigDecimal("200"));
    }
}
