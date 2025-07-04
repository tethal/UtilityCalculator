package utilcalc.core.reportGen;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import utilcalc.core.model.DateRange;
import utilcalc.core.model.input.ServiceCost;

final class ReportGenUtil {

    private ReportGenUtil() {}

    static List<ServiceCost> splitServiceCostsByReport(
            DateRange reportDateRange, List<ServiceCost> serviceCosts) {

        validateServiceCostCoverage(reportDateRange, serviceCosts);

        List<ServiceCost> result = new ArrayList<>();

        for (ServiceCost cost : serviceCosts) {
            cost.dateRange()
                    .intersect(reportDateRange)
                    .ifPresent(overlap -> result.add(new ServiceCost(overlap, cost.annualCost())));
        }

        return result;
    }

    static List<ServiceCost> splitServiceCostsByMonths(List<ServiceCost> serviceCosts) {
        List<ServiceCost> result = new ArrayList<>();

        for (ServiceCost cost : serviceCosts) {
            List<DateRange> months = cost.dateRange().splitByMonths();

            for (DateRange monthRange : months) {
                result.add(new ServiceCost(monthRange, cost.annualCost()));
            }
        }

        return result;
    }

    private static void validateServiceCostCoverage(
            DateRange reportDateRange, List<ServiceCost> serviceCosts) {

        List<ServiceCost> sortedServiceCosts =
                serviceCosts.stream()
                        .sorted(Comparator.comparing(cost -> cost.dateRange().startDate()))
                        .toList();

        LocalDate sortedFirstStart = sortedServiceCosts.getFirst().dateRange().startDate();
        LocalDate sortedLastEnd = sortedServiceCosts.getLast().dateRange().endDateExclusive();

        if (sortedFirstStart.isAfter(reportDateRange.startDate())
                || sortedLastEnd.isBefore(reportDateRange.endDateExclusive())) {
            throw new IllegalArgumentException(
                    "ServiceCosts do not fully cover the report date interval.");
        }

        for (int i = 0; i < sortedServiceCosts.size() - 1; i++) {
            LocalDate currentEnd = sortedServiceCosts.get(i).dateRange().endDateExclusive();
            LocalDate nextStart = sortedServiceCosts.get(i + 1).dateRange().startDate();

            if (!currentEnd.equals(nextStart)) {
                throw new IllegalArgumentException(
                        String.format(
                                "ServiceCosts do not connect seamlessly or they overlap: %s and %s",
                                sortedServiceCosts.get(i), sortedServiceCosts.get(i + 1)));
            }
        }
    }
}
