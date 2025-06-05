package utilcalc.core.model.output;

import java.math.BigDecimal;

public interface Payment {

    String description();

    BigDecimal count();

    BigDecimal unitAmount();

    BigDecimal amount();
}
