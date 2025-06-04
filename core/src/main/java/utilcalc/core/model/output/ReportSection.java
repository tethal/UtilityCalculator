package utilcalc.core.model.output;

import java.math.BigDecimal;

/**
 * Abstract class for a section in the report.
 *
 * <p>Each section has a name and a total amount. Subclasses should define specific types of
 * sections.
 *
 * <p>This class is immutable.
 */
public abstract class ReportSection {

    private final String name;

    private final BigDecimal totalAmount;

    protected ReportSection(String name, BigDecimal totalAmount) {
        this.name = name;
        this.totalAmount = totalAmount;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

}
