package utilcalc.core.model.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class HeatingPaymentTest {

    @Test
    void testHeatingPaymentsField() {
        HeatingPayment heatingPayment = TestOutputDataFactory.createHeatingPayment();

        assertEquals("Únor", heatingPayment.getDescription());
        assertEquals(new BigDecimal("0.5"), heatingPayment.getCount());
        assertEquals(new BigDecimal("879.36"), heatingPayment.getUnitAmount());
        assertEquals(new BigDecimal("10992"), heatingPayment.getAmount());
        assertEquals(new BigDecimal("0.16"), heatingPayment.getCoefficient());
    }

}
