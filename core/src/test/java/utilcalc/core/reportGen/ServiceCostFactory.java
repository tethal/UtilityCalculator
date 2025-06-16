package utilcalc.core.reportGen;

import java.math.BigDecimal;
import java.time.LocalDate;
import utilcalc.core.model.input.ServiceCost;

final class ServiceCostFactory {

    private ServiceCostFactory() {}

    public static ServiceCost validOneFullYearServiceCost() {
        return new ServiceCost(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), new BigDecimal("8772"));
    }

    public static ServiceCost validOneAndHalfYearServiceCost() {
        return new ServiceCost(
                LocalDate.of(2024, 1, 1), LocalDate.of(2025, 6, 15), new BigDecimal("8000"));
    }

    public static ServiceCost validPartialMonthServiceCost() {
        return new ServiceCost(
                LocalDate.of(2024, 1, 6), LocalDate.of(2024, 1, 16), new BigDecimal("8000"));
    }

    public static ServiceCost validTwoPartialMonthsServiceCost() {
        return new ServiceCost(
                LocalDate.of(2024, 12, 6), LocalDate.of(2025, 1, 16), new BigDecimal("8000"));
    }
}
