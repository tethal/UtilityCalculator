package utilcalc.core.model.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import utilcalc.core.model.Payment;

public class OtherFeeSectionTest {

    @Test
    void testOtherFeeSectionFields() {
        OtherFeeSection otherFeeSection = TestOutputDataFactory.createOtherFeeSection();

        assertEquals("Ostatní", otherFeeSection.getName());
        assertEquals(new BigDecimal("12000"), otherFeeSection.getTotalAmount());

        for (Payment payment : otherFeeSection.getOtherFees()) {
            assertEquals("Leden - Prosinec", payment.getDescription());
            assertEquals(new BigDecimal("12"), payment.getCount());
            assertEquals(new BigDecimal("500"), payment.getUnitAmount());
            assertEquals(new BigDecimal("6000"), payment.getAmount());
        }
    }
}
