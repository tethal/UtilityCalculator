package utilcalc.core.reportGen;

import static utilcalc.core.model.OpenIntervalFactory.*;

import java.math.BigDecimal;
import utilcalc.core.model.OpenInterval;
import utilcalc.core.model.input.ServiceCost;

final class ServiceCostFactory {
    private static final OpenInterval FULL_2024_YEAR_INTERVAL = full2024Interval();
    private static final OpenInterval FULL_2025_YEAR_INTERVAL = full2025Interval();

    private static final BigDecimal SERVICE_COST_2024 = new BigDecimal("8772");
    private static final BigDecimal SERVICE_COST_2025 = new BigDecimal("8000");

    private ServiceCostFactory() {}

    /** [ 1.1.2024 ; 1.1.2025) cost: 8772 */
    public static ServiceCost valid2024ServiceCost() {
        return new ServiceCost(
                FULL_2024_YEAR_INTERVAL.startDate(),
                FULL_2024_YEAR_INTERVAL.endDateExclusive(),
                SERVICE_COST_2024);
    }

    /** [ 1.1.2025 ; 1.1.2026) cost: 8000 */
    public static ServiceCost valid2025ServiceCost() {
        return new ServiceCost(
                FULL_2025_YEAR_INTERVAL.startDate(),
                FULL_2025_YEAR_INTERVAL.endDateExclusive(),
                SERVICE_COST_2025);
    }

    /** [ 15.1.2024 ; 15.1.2025 ) cost: 8772 */
    public static ServiceCost partialServiceCost() {
        OpenInterval twoPartialMonthsInterval = twoPartialMonthsInterval();

        return new ServiceCost(
                twoPartialMonthsInterval.startDate(),
                twoPartialMonthsInterval.endDateExclusive(),
                SERVICE_COST_2024);
    }
}
