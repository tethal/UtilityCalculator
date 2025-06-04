package utilcalc.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.output.TestOutputDataFactory;

public class PaymentTest {

    @Test
    void testPaymentFields() {
        Payment payment = TestOutputDataFactory.createPayment();

        assertEquals("Leden - Prosinec", payment.getDescription());
        assertEquals(new BigDecimal("12"), payment.getCount());
        assertEquals(new BigDecimal("500"), payment.getUnitAmount());
        assertEquals(new BigDecimal("6000"), payment.getAmount());
    }
}
