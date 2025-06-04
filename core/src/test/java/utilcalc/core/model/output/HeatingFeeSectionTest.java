package utilcalc.core.model.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class HeatingFeeSectionTest {

    @Test
    void testHeatingFeeSectionFields() {
        HeatingFeeSection heatingFeeSection = TestOutputDataFactory.createHeatingFeeSection();

        assertEquals("Vytápění", heatingFeeSection.getName());
        assertEquals(new BigDecimal("8000"), heatingFeeSection.getTotalAmount());

        for (HeatingPayment heatingPayment : heatingFeeSection.getHeatingFees()) {
            assertEquals("Únor", heatingPayment.getDescription());
            assertEquals(new BigDecimal("0.5"), heatingPayment.getCount());
            assertEquals(new BigDecimal("879.36"), heatingPayment.getUnitAmount());
            assertEquals(new BigDecimal("10992"), heatingPayment.getAmount());
            assertEquals(new BigDecimal("0.16"), heatingPayment.getCoefficient());
        }
    }
}
