package utilcalc.core.model.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.Payment;

public class DepositSectionTest {

    @Test
    void testDepositSectionFields() {
        DepositSection depositSection = TestOutputDataFactory.createDepositSection();

        assertEquals("Zálohy", depositSection.getName());
        assertEquals(new BigDecimal("6000"), depositSection.getTotalAmount());

        for (Payment payment : depositSection.getPayments()) {
            assertEquals("Leden - Prosinec", payment.getDescription());
            assertEquals(new BigDecimal("12"), payment.getCount());
            assertEquals(new BigDecimal("500"), payment.getUnitAmount());
            assertEquals(new BigDecimal("6000"), payment.getAmount());
        }
    }

}
